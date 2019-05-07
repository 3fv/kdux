import {Button, Input, FlipperPlugin, FlexColumn, styled, Text} from 'flipper'
import type {Notification} from 'flipper/plugin'
import JSONTree from "./JSONTree";
import React from 'react'

type DisplayMessageResponse = {
  greeting: string,
}

type S = {
  prompt: string,
  message: string,
}


type PersistedState = {
  currentNotificationIds: Array<number>,
  receivedMessage: ?string,
  storeState: Object
}

type P = {
  persistedState:PersistedState
}

const Container = styled(FlexColumn)({
  alignItems: 'left',
  justifyContent: 'space-around',
  padding: 20,
})

export default class extends FlipperPlugin<S, *, P> {
  static defaultPersistedState = {
    currentNotificationIds: [],
    receivedMessage: null,
    storeState: {}
  }

  state = {
    prompt: 'Type a message below to see it displayed on the mobile app',
    message: '',
  }

  /*
   * Reducer to process incoming "send" messages from the mobile counterpart.
   */
  static persistedStateReducer = (
    persistedState: PersistedState,
    method: string,
    payload: Object,
  ): PersistedState => {
    if (method === "sendState") {
      const storeState = typeof payload[0] === 'string' ? JSON.parse(payload[0]) : {}
      console.log("State received", storeState)
      return {
        ...persistedState,
        storeState
      }
    } else if (method === 'triggerNotification') {
      return {
        ...persistedState,
        currentNotificationIds: persistedState.currentNotificationIds.concat([
          payload.id,
        ]),
      }
    }
    if (method === 'displayMessage') {
      return {
        ...persistedState,
        receivedMessage: payload.msg,
      }
    }
    return persistedState || {}
  }

  /*
   * Callback to provide the currently active notifications.
   */
  static getActiveNotifications = (
    persistedState: PersistedState,
  ): Array<Notification> => {
    return persistedState.currentNotificationIds.map((x: number) => {
      return {
        id: 'test-notification:' + x,
        message: 'Example Notification',
        severity: 'warning',
        title: 'Notification: ' + x,
      }
    })
  }

  onStoreState = (params:Object) => {

  }

  componentDidMount(): void {
    //this.client.subscribe("sendState",)
  }

  componentWillUnmount(): void {
  }

  /*
   * Call a method of the mobile counterpart, to display a message.
   */
  sendState(...args) {
    console.log("Received",...args)
    // this.client
    //   .call('displayMessage', {message: this.state.message || 'Weeeee!'})
    //   .then((params: DisplayMessageResponse) => {
    //     this.setState({
    //       prompt: 'Nice',
    //     })
    //   })
  }

  render() {
    const
      {props, state} = this,
      {persistedState} = this.props,
      {storeState} = persistedState

    console.log("Render",props,state)
    return (
      <Container>
        <JSONTree data={storeState}/>
      </Container>
    )
  }
}
