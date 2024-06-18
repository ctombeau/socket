package com.socket.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerMT extends Thread{
    private int nombreClient;
    private List<Conversation> clients = new ArrayList<Conversation>();
	public static void main(String[] args) {
		new ServerMT().start();
	}
	
	@Override
	public void run() {
		try {
		 ServerSocket ss = new ServerSocket(1234);
		 System.out.println("Demarrage du serveur...");
		 while(true) {
			 Socket socket = ss.accept();
			 ++nombreClient;
			 Conversation conversation = new Conversation(socket, nombreClient);
			 clients.add(conversation);
			 conversation.start();
			 //new Conversation(socket, nombreClient).start();
		 }
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
    
	class Conversation extends Thread{
		protected Socket socket; 
		protected int numeroClient;
		public Conversation(Socket socket, int numeroClient) {
			this.socket = socket;
			this.numeroClient=numeroClient;
		}
		
		public void broadcastMessage(String message, Socket socket,int numeroClient) {
			
			try {
				for(Conversation client : clients) {
					if(client.socket!=socket)
					{
						if(client.numeroClient==numeroClient || numeroClient==-1) {
							PrintWriter pw = new PrintWriter(client.socket.getOutputStream(),true);
							 pw.println(message);
						}
					 
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			try {
				InputStream is = socket.getInputStream();
				InputStreamReader isr = new InputStreamReader(is); 
				BufferedReader br = new BufferedReader(isr);
				
				OutputStream os = socket.getOutputStream();
				PrintWriter pw = new PrintWriter(os,true);
				String IP = socket.getRemoteSocketAddress().toString();
				System.out.println("Connexion du client numero "+numeroClient + " IP="+IP);
				pw.println("BienVenue, vous etes le client numero "+numeroClient);
				while(true){
					String req = br.readLine();
					if(req.contains("=>")) {
						String[] requestParams = req.split("=>");
						if(requestParams.length==2) {
							String message = requestParams[1];
							int numeroClient=Integer.parseInt(requestParams[0]);
							broadcastMessage(message, socket,numeroClient);
						}
						
					}
					else
						broadcastMessage(req,socket,-1);
					
					//System.out.println("Le client "+IP+ " a envoye une requete "+req);
					//pw.println(req.length());
				}
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}
