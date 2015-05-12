package com.example.stewardimperial.socketprogramming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.example.stewardimperial.presentors.ACTOrder;
import com.example.stewardimperial.presentors.ACTTableGridLayout;
import com.example.stewardimperial.utils.UTILConstants;

public class TCPReceiver extends Thread {

	Context context;
	Handler handler;
	ServerSocket serverSocket=null;
	String tcpIp;
	Socket socket;

	private static InputStreamReader inputStreamReader;
	private static BufferedReader bufferedReader;
	private static String message;

	public TCPReceiver(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
	}

	public void run() {
		startTCPServer(UTILConstants.TCPRECEIVERPORT);
	}

	private void startTCPServer(int receiverPort) {
		socket = null;
		try {
			serverSocket  = new ServerSocket(receiverPort);
		} catch (IOException e) {
//			e.printStackTrace();
		}
		while(!Thread.currentThread().isInterrupted()){
			try {
				socket = serverSocket.accept();
				inputStreamReader = new InputStreamReader(socket.getInputStream());
				bufferedReader = new BufferedReader(inputStreamReader);
				message = bufferedReader.readLine();
				invokeOnPing();
				
				socket.close();

				System.out.println(message);
			} catch (IOException e) {
//				e.printStackTrace();
			}	
		}
	}


	private void invokeOnPing(){
		if (UTILConstants.ACTIVITY.getClass().getName().equals("com.example.stewardimperial.presentors.ACTOrder")) {

			UTILConstants.HANDLER.post(new Runnable() {

				@Override
				public void run() {
					((ACTOrder)UTILConstants.ACTIVITY).onOrderCooked();		
				}
			});

		}else if (UTILConstants.ACTIVITY.getClass().getName().equals("com.example.stewardimperial.presentors.ACTTableGridLayout")) {

			UTILConstants.HANDLER.post(new Runnable() {

				@Override
				public void run() {
					((ACTTableGridLayout)UTILConstants.ACTIVITY).onOrderCooked();		
				}
			});

		}else if(UTILConstants.ACTIVITY.getClass().getName().equals("com.example.stewardimperial.presentors.ACTLogin")) {
			
		}
	}


	public void closeExistingSocket(){
		if(serverSocket!=null){
			try {
				Log.v("socket_programming", "tcp server socket is not null so closing");
				serverSocket.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}else{
			Log.v("socket_programming", "tcp server socket is already null");
		}
	}

}
