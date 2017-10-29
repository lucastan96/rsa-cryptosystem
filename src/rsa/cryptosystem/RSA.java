package rsa.cryptosystem;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class RSA {

    public static void main(String[] args) {
	BigInteger[] publicKey = new BigInteger[2];
	BigInteger[] privateKey = new BigInteger[2];

	int option = 0;

	while (option != 5) {
	    System.out.println("\n**** Menu *******************************");
	    System.out.println("1. Configure Keys");
	    System.out.println("2. Check Keys");
	    System.out.println("3. Encrypt Message");
	    System.out.println("4. Decrypt Message");
	    System.out.println("5. Exit");

	    option = getOption();

	    if (option == 1) {
		setKeys(publicKey, privateKey);
	    } else if (option == 2) {
		getKeys(publicKey, privateKey);
	    } else if (option == 3) {
		encryptMessage(publicKey, privateKey);
	    } else if (option == 4) {
		decryptMessage(publicKey, privateKey);
	    } else {
		System.out.println("\nApplication exited successfully.");
		break;
	    }
	}
    }

    public static void setKeys(BigInteger[] publicKey, BigInteger[] privateKey) {
	Scanner in = new Scanner(System.in);

	System.out.print("\nPlease enter the first key: ");
	int num1 = in.nextInt();
	BigInteger key1 = new BigInteger(Integer.toString(num1));

	while (!key1.isProbablePrime(1)) {
	    System.out.println("Error: " + num1 + " is not a prime number, please try again.");
	    System.out.print("Please enter the first key: ");
	    key1 = new BigInteger(Integer.toString(in.nextInt()));
	}

	System.out.print("Please enter the second key: ");
	int num2 = in.nextInt();
	BigInteger key2 = new BigInteger(Integer.toString(num2));

	while (num2 == num1 || !key2.isProbablePrime(1)) {
	    if (num2 == num1) {
		System.out.println("Error: First key and second key cannot be the same, please try again.");
		System.out.print("Please enter the second key: ");
		num2 = in.nextInt();
		key2 = new BigInteger(Integer.toString(num2));
	    }
	    if (!key2.isProbablePrime(1)) {
		System.out.println("Error: " + num2 + " is not a prime number, please try again.");
		System.out.print("Please enter the second key: ");
		num2 = in.nextInt();
		key2 = new BigInteger(Integer.toString(num2));
	    }
	}

	System.out.print("Please enter the third key: ");
	int num3 = in.nextInt();
	BigInteger key3 = new BigInteger(Integer.toString(num3));

	while (num3 == num1 || num3 == num2) {
	    System.out.println("Error: Keys must be unique, please try again.");
	    System.out.print("Please enter the third key: ");
	    num3 = in.nextInt();
	}

	BigInteger pKey1 = key1.multiply(key2);
	BigInteger phi_pKey1 = key1.subtract(BigInteger.ONE);
	BigInteger phi_pKey2 = key2.subtract(BigInteger.ONE);
	BigInteger temp = phi_pKey1.multiply(phi_pKey2);
	BigInteger gcd = key3.gcd(temp);

	while (!gcd.equals(BigInteger.ONE)) {
	    System.out.println("Error: " + num3 + " is not co-prime, please try again: ");
	    System.out.print("Please enter the third key: ");
	    num3 = in.nextInt();

	    while (num3 == num1 || num3 == num2) {
		System.out.println("Error: Keys must be unique, please try again.");
		System.out.print("Please enter the third key: ");
		num3 = in.nextInt();
	    }

	    key3 = new BigInteger(Integer.toString(num3));
	    gcd = key3.gcd(temp);
	}

	BigInteger key4 = key3.modInverse(temp);

	publicKey[0] = pKey1;
	publicKey[1] = key3;

	privateKey[0] = pKey1;
	privateKey[1] = key4;

	System.out.println("\nKeys configuration successful.");
    }

    public static void getKeys(BigInteger[] publicKey, BigInteger[] privateKey) {
	if (publicKey[0] == null || privateKey[0] == null) {
	    System.out.println("\nError: Keys not configured yet.");
	} else {
	    System.out.println("\nPublic Key : " + publicKey[0] + ", " + publicKey[1]);
	    System.out.println("Private Key : " + privateKey[0] + ", " + privateKey[1]);
	}
    }

    public static void encryptMessage(BigInteger[] publicKey, BigInteger[] privateKey) {
	if (publicKey[0] == null || privateKey[0] == null) {
	    System.out.println("\nError: Keys not configured yet.");
	} else {
	    Scanner in = new Scanner(System.in);

	    System.out.println("\nPlease enter message: ");
	    String message = in.nextLine();

	    char[] unknown = message.toCharArray();
	    ArrayList<String> charToNum = new ArrayList<>();

	    for (int i = 0; i < unknown.length; i++) {
		String temp = Integer.toString((int) unknown[i]);
		charToNum.add(temp);
	    }

	    ArrayList<String> encodeNum = new ArrayList<>();
	    int length = publicKey[1].toString().length();

	    for (int i = 0; i < charToNum.size(); i++) {
		BigInteger current = new BigInteger(charToNum.get(i));
		BigInteger process = current.modPow(publicKey[1], publicKey[0]);
		String result = process.toString();
		while (result.length() <= length) {
		    result = "0" + result;
		}
		encodeNum.add(result);
	    }

	    String cipher = "";

	    for (int i = 0; i < encodeNum.size(); i++) {
		cipher += encodeNum.get(i);
	    }

	    System.out.println("\nThe encrypted message is:");
	    System.out.println(cipher);
	}
    }

    public static void decryptMessage(BigInteger[] publicKey, BigInteger[] privateKey) {
	if (publicKey[0] == null || privateKey[0] == null) {
	    System.out.println("\nError: Keys not configured yet.");
	} else {
	    Scanner in = new Scanner(System.in);

	    System.out.println("\nPlease enter encrypted message: ");
	    String input = in.nextLine();

	    int length = publicKey[0].toString().length();
	    ArrayList<String> number = new ArrayList<>();

	    for (int i = 0; i < input.length(); i += length) {
		String temp = input.substring(i, i + length);
		number.add(temp);
	    }

	    ArrayList<String> decrypt = new ArrayList<>();

	    for (int i = 0; i < number.size(); i++) {
		BigInteger num = new BigInteger(number.get(i));
		BigInteger process = num.modPow(privateKey[1], privateKey[0]);
		String result = process.toString();
		decrypt.add(result);
	    }

	    String message = "";

	    for (int i = 0; i < decrypt.size(); i++) {
		int pop = Integer.parseInt(decrypt.get(i));
		char letter = (char) pop;
		message += letter;
	    }

	    System.out.println("\nThe decoded message is:");
	    System.out.println(message);
	}
    }

    public static int getOption() {
	Scanner s = new Scanner(System.in);
	System.out.print("\nPlease enter your option: ");
	try {
	    int option = s.nextInt();
	    s.nextLine();
	    return option;
	} catch (InputMismatchException e) {
	    System.out.println("Error: Option input must be an integer.");
	    int option = getOption();
	    return option;
	}
    }
}
