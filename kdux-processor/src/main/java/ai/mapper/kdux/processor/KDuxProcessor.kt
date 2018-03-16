package ai.mapper.kdux.processor

import ai.mapper.kdux.annotations.ActionReducer
import ai.mapper.kdux.annotations.ActionsBuilder
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic


/**
 * Processes all @ActionsBuilder @ActionReducers
 */
@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes(value = [
  "ai.mapper.kdux.annotations.ActionReducer",
  "ai.mapper.kdux.annotations.ActionsBuilder"
])
class KDuxProcessor : AbstractProcessor() {

  companion object {
    private val ANNO_ACTION_BUILDER = ActionsBuilder::class.java
    private val ANNO_ACTION_REDUCER = ActionReducer::class.java
    private const val GENERATE_KOTLIN_CODE_OPTION = "generate.kotlin.code"
    private const val KAPT_KOTLIN_GENERATED_OPTION = "kapt.kotlin.generated"
  }

  /**
   * For communicating diagnostics
   */
  private lateinit var messager: Messager

  /**
   * Log info message
   */
  private fun info(msg:String) {
    messager.printMessage(Diagnostic.Kind.NOTE, msg)
  }

  /**
   * Log error message
   */
  private fun error(msg:String) {
    messager.printMessage(Diagnostic.Kind.ERROR, msg)
  }

  /**
   * Init the processor
   */
  override fun init(processingEnv: ProcessingEnvironment) {
    super.init(processingEnv)
    messager = processingEnv.messager
    info("Initializing processor")
  }

  /**
   * Process annotations
   */
  override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
    roundEnv.getElementsAnnotatedWith(ANNO_ACTION_BUILDER)
      .filterIsInstance<TypeElement>()
      .filter { isValidClass(it) }
      .forEach {
        generateActions(it,roundEnv)
      }

    return true
  }

  /**
   * Filter valid classes only
   */
  private fun isValidClass(annotatedClass: TypeElement): Boolean {

    // if class is not a public class
    if (!annotatedClass.modifiers.contains(Modifier.PUBLIC)) {
      error("Classes annotated with ${ANNO_ACTION_BUILDER.simpleName} must be public.")
      return false
    }

    // if the class is a abstract class
    if (!annotatedClass.modifiers.contains(Modifier.ABSTRACT)) {
      error("Classes annotated with ${ANNO_ACTION_BUILDER.simpleName} must be abstract.")
      return false
    }

    return true
  }

  /**
   * Generate action classes
   */
  private fun generateActions(element: TypeElement, @Suppress("UNUSED_PARAMETER") roundEnv:RoundEnvironment) {
    val elementUtils = processingEnv.elementUtils
    //val filer = processingEnv.filer

    val options = processingEnv.options
    val generatedFileSuffix = "Generated"
    //val generateKotlinCode = "true" == options[GENERATE_KOTLIN_CODE_OPTION]
    val kotlinGenerated = options[KAPT_KOTLIN_GENERATED_OPTION]

    val packageName = elementUtils.getPackageOf(element).qualifiedName.toString()
    val simpleName = element.simpleName.toString()
    val generatedClassName = simpleName.capitalize() + generatedFileSuffix


    val reducerElements = element.enclosedElements
      .filterIsInstance<ExecutableElement>()
      .filter { it.getAnnotation(ANNO_ACTION_REDUCER) != null }
      .map { it }


    //val clazz = ClassName(packageName,generatedClassName)
    val fileSpec = FileSpec.builder(packageName,generatedClassName)
    val typeSpec = TypeSpec.Companion.classBuilder(generatedClassName)
      .superclass(ClassName(packageName,element.simpleName.toString()))
      .primaryConstructor(FunSpec.constructorBuilder().build())


    //info("generate ${generatedClassName} ${generateKotlinCode} ${options}")

    // REDUCERS
    reducerElements.forEach {
      if (it.modifiers.contains(Modifier.FINAL))
        error("${element.simpleName}.${it.simpleName} is FINAL, make open")

      val reducerSpec = FunSpec
        .builder(it.simpleName.toString())
        .addModifiers(KModifier.OVERRIDE)
        .returns(it.returnType.asTypeName())

      // PARAMETERS
      val parameterNames = it.parameters.map { it.simpleName.toString() }
      it.parameters.forEach {param ->
        val paramTypeName = param.asType().asTypeName()
        reducerSpec.addParameter(param.simpleName.toString(),paramTypeName)
      }

      //RETURNS
      reducerSpec.addStatement("val reducer = super.${it.simpleName}(${parameterNames.joinToString(",")})")
      reducerSpec.addStatement("getStore().dispatch(this,reducer)")
      reducerSpec.addStatement("@Suppress(\"CAST_NEVER_SUCCEEDS\")")
      reducerSpec.addStatement("return ai.mapper.kdux.reducers.NoCallReducer(reducer)")

      typeSpec.addFunction(reducerSpec.build())
    }

    fileSpec.addType(typeSpec.build()).build().writeTo(File(kotlinGenerated))

  }
}

data class ActionsClass(val typeElement: TypeElement, val variableNames: List<String>)