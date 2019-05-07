"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

var _flipper = require("flipper");

var _JSONTree = _interopRequireDefault(require("./JSONTree"));

var _react = _interopRequireDefault(require("react"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _objectSpread(target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i] != null ? arguments[i] : {}; var ownKeys = Object.keys(source); if (typeof Object.getOwnPropertySymbols === 'function') { ownKeys = ownKeys.concat(Object.getOwnPropertySymbols(source).filter(function (sym) { return Object.getOwnPropertyDescriptor(source, sym).enumerable; })); } ownKeys.forEach(function (key) { _defineProperty(target, key, source[key]); }); } return target; }

function _defineProperty(obj, key, value) { if (key in obj) { Object.defineProperty(obj, key, { value: value, enumerable: true, configurable: true, writable: true }); } else { obj[key] = value; } return obj; }

const Container = (0, _flipper.styled)(_flipper.FlexColumn)({
  alignItems: 'left',
  justifyContent: 'space-around',
  padding: 20
});

class _class extends _flipper.FlipperPlugin {
  constructor() {
    super(...arguments);

    _defineProperty(this, "state", {
      prompt: 'Type a message below to see it displayed on the mobile app',
      message: ''
      /*
       * Reducer to process incoming "send" messages from the mobile counterpart.
       */

    });

    _defineProperty(this, "onStoreState", params => {});
  }

  componentDidMount() {//this.client.subscribe("sendState",)
  }

  componentWillUnmount() {}
  /*
   * Call a method of the mobile counterpart, to display a message.
   */


  sendState() {
    for (var _len = arguments.length, args = new Array(_len), _key = 0; _key < _len; _key++) {
      args[_key] = arguments[_key];
    }

    console.log("Received", ...args); // this.client
    //   .call('displayMessage', {message: this.state.message || 'Weeeee!'})
    //   .then((params: DisplayMessageResponse) => {
    //     this.setState({
    //       prompt: 'Nice',
    //     })
    //   })
  }

  render() {
    const props = this.props,
          state = this.state,
          persistedState = this.props.persistedState,
          storeState = persistedState.storeState;
    console.log("Render", props, state);
    return _react.default.createElement(Container, null, _react.default.createElement(_JSONTree.default, {
      data: storeState
    }));
  }

}

exports.default = _class;

_defineProperty(_class, "defaultPersistedState", {
  currentNotificationIds: [],
  receivedMessage: null,
  storeState: {}
});

_defineProperty(_class, "persistedStateReducer", (persistedState, method, payload) => {
  if (method === "sendState") {
    const storeState = typeof payload[0] === 'string' ? JSON.parse(payload[0]) : {};
    console.log("State received", storeState);
    return _objectSpread({}, persistedState, {
      storeState
    });
  } else if (method === 'triggerNotification') {
    return _objectSpread({}, persistedState, {
      currentNotificationIds: persistedState.currentNotificationIds.concat([payload.id])
    });
  }

  if (method === 'displayMessage') {
    return _objectSpread({}, persistedState, {
      receivedMessage: payload.msg
    });
  }

  return persistedState || {};
});

_defineProperty(_class, "getActiveNotifications", persistedState => {
  return persistedState.currentNotificationIds.map(x => {
    return {
      id: 'test-notification:' + x,
      message: 'Example Notification',
      severity: 'warning',
      title: 'Notification: ' + x
    };
  });
});