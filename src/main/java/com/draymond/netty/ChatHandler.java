package com.draymond.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;


public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static ChannelGroup clients=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        //客户端传过来的消息
        String content = textWebSocketFrame.text();
        System.out.println("接收到的消息"+content);

        //对每一个Channel发消息
        //clients.writeAndFlush(new TextWebSocketFrame("[服务器接收到消息：]"+ LocalDateTime.now())+",消息为："+content);

        for(Channel channel:clients){
            channel.writeAndFlush(new TextWebSocketFrame("[服务器接收到消息：]"+ LocalDateTime.now()+",消息为："+content));
        }
    }


    /**
     * 客户端连接服务端之后（打开连接）
     * 获得客户端的channel.并且放到ChannelGroup
     **/
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        clients.add(ctx.channel());
    }




    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //将客户端的channel一出ChannelGroup
       // clients.remove(ctx.channel());
        System.out.println("客户端断开，channel对应的长id"+ctx.channel().id().asLongText());
        System.out.println("客户端断开，channel对应的短id"+ctx.channel().id().asShortText());
    }
}
