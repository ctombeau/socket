package com.socket.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMT extends Thread{
    private int nombreClient;
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
			 new Conversation(socket, nombreClient).start();
		 }
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
    
	class Conversation extends Thread{
		private Socket socket; 
		private int numeroClient;
		public Conversation(Socket socket, int numeroClient) {
			this.socket = socket;
			this.numeroClient=numeroClient;
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
					System.out.println("Le client "+IP+ " a envoye une requete "+req);
					pw.println(req.length());
				}
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}
