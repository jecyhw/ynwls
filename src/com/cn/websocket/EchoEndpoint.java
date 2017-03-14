package com.cn.websocket;

import javax.websocket.*;
import java.io.IOException;

/**
 * Created by SNNU on 2015/5/9.
 */
public class EchoEndpoint extends Endpoint {
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        RemoteEndpoint.Basic remoteEndpointBasic = session.getBasicRemote();
        session.addMessageHandler(new EchoMessageHandler(remoteEndpointBasic));
    }

    private class EchoMessageHandler implements MessageHandler.Whole<String>  {
        private final RemoteEndpoint.Basic remoteEndpointBasic;
        public EchoMessageHandler(RemoteEndpoint.Basic remoteEndpointBasic) {
            this.remoteEndpointBasic = remoteEndpointBasic;
        }

        @Override
        public void onMessage(String msg) {
            try {
                if (remoteEndpointBasic != null) {
                    remoteEndpointBasic.sendText(msg);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
