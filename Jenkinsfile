


node("master") {
  
  String branchName = env.BRANCH_NAME
  
  /**
   * Cleanup
   */
  stage("Clean") {
    sh """
    rm -Rf \$(find . -name build) || true
    rm -Rf ./out || true
    """
  }
  
  /**
   * Checkout
   */
  stage("Checkout: ${branchName}") {
    checkout scm
  }
  
  if (ciSkip(action: 'should-skip')) {
    return
  }
  
  /**
   * Build & Test
   */
  stage("Build & Test") {
    echo "Building: ${env.BUILD_NUMBER}"
    sh """
    #./gradlew test
    ./gradlew build
    """

//    junit "**/build/test-results/test/*.xml"
  }
  
  /**
   * Publish for master / develop
   */
  if (['master', 'develop'].contains(branchName)) {
    stage("Publish: ${branchName}") {
      
      // BUMP VERSION
      versionBump()
      
      sh """
      ./gradlew --stacktrace --info :kdux-core:publish
      ./gradlew --stacktrace --info :kdux-processor:publish
      ./gradlew --stacktrace --info :kdux-android:publish
      """
    }
  }
}

