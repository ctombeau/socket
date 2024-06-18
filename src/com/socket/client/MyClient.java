package com.socket.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class MyClient {

	public static void main(String[] args) throws IOException {
		System.out.println("Je me connecte au server...");
		Socket s = new Socket("localhost",1234); 
		InputStream is = s.getInputStream();
		OutputStream os = s.getOutputStream();
		
		Scanner sc = new Scanner(System.in);
		System.out.print("Donner un nombre: ");
		int nb = sc.nextInt();
		System.out.println("J'envoie le nombre "+ nb + " au serveur");
		os.write(nb);
		System.out.println("J'attends la reponse du serveur");
		int res = is.read();
		System.out.println("Reponse du serveur: "+res);
	}

}
