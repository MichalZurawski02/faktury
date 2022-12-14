package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class view {
  private final invoiceController controller;
  
  public view(invoiceController controller) {
    this.controller = controller;
  }
  public ArrayList<String> insertCompanyData(Scanner scanner) {
    System.out.println("Wprowadz nazwe firmy: ");
    String name = scanner.nextLine();
    System.out.println("Wprowadz nip firmy: ");
    int nip = Integer.parseInt(scanner.nextLine());
    System.out.println("Wprowadz adres firmy: ");
    String address = scanner.nextLine();
    return new ArrayList<>(Arrays.asList(name, Integer.toString(nip), address));
  }
  public void insertPersonData(Scanner scanner) {
    System.out.println("Wprowadz imie nabywcy: ");
    String name = scanner.nextLine();
    System.out.println("Wprowadz nazwisko nabywcy: ");
    String lastName = scanner.nextLine();
    System.out.println("Wprowadz adres nabywcy: ");
    String address = scanner.nextLine();
    controller.addBuyer(name, lastName, address);
  }
  public void insertElementData(Scanner scanner) {
    System.out.println("Wprowadz nazwe produktu: ");
    String name = scanner.nextLine();
    
    System.out.println("Wprowadz cene netto: ");
    double price = Double.parseDouble(scanner.nextLine());
    while(price <= 0) {
      System.out.println("Bledna cena. Wprowadz poprawna cene: ");
      price = Double.parseDouble(scanner.nextLine());
    }
    
    System.out.println("Wprowadz ilosc: ");
    int quantity = Integer.parseInt(scanner.nextLine());
    while(quantity <= 0) {
      System.out.println("Bledna ilosc. Wprowadz poprawna ilosc: ");
      quantity = Integer.parseInt(scanner.nextLine());
    }
    
    System.out.println("Wprowadz stawke VAT(np.: 0.23): ");
    double vat = Double.parseDouble(scanner.nextLine());
    while(vat < 0 | vat > 1) {
      System.out.println("Bledna stawka. Wprowadz poprawna stawke VAT: ");
      vat = Double.parseDouble(scanner.nextLine());
    }
    controller.addElement(price, vat, name, quantity);
  }
  
  public void display() {
    System.out.println("Sprzedawca: ");
    System.out.println(controller.getNumber() + "/" + controller.getDate());
    System.out.println(controller.getIssuer().getCompanyName() + "\n"
            + controller.getIssuer().getNip() + "\n"
            + controller.getIssuer().getAddress());
    
    System.out.println("Nabywca: ");
    System.out.println(controller.getBuyer().getParams().get(0));
    System.out.println(controller.getBuyer().getParams().get(1));
    System.out.println(controller.getBuyer().getAddress());
    
    System.out.println("Pozycje faktury:");
    System.out.println("LP | Nazwa towaru/uslugi | Ilosc | Cena netto | Wartosc netto | VAT | Wartosc VAT | Wartosc brutto");
    for(int i = 0; i < controller.getElementList().size(); i++) {
      double nettoValue = controller.getElementList().get(i).getItemModel().getNettoPrice()
                          * controller.getElementList().get(i).getQuantity();
      double vatValue = controller.getElementList().get(i).getItemModel().getVat()
                        * controller.getElementList().get(i).getItemModel().getNettoPrice()
                        * controller.getElementList().get(i).getQuantity();
      System.out.println(
              i + 1 + " | "
              + controller.getElementList().get(i).getItemModel().getName() + " | "
              + controller.getElementList().get(i).getQuantity() + " | "
              + controller.getElementList().get(i).getItemModel().getNettoPrice() + " | "
              + nettoValue + " | "
              + controller.getElementList().get(i).getItemModel().getVat() + " | "
              + vatValue + " | "
              + (nettoValue + vatValue));
    }
  }
  
  public void start() {
    Scanner scanner = new Scanner(System.in);
    
    System.out.println("Wprowadz number faktury: ");
    int number = Integer.parseInt(scanner.nextLine());
    System.out.println("Wprowadz date wystawienia faktury: ");
    String date = scanner.nextLine();
    controller.createInvoice(number, date);
  
    System.out.println("Typ nabywcy osoba prywatna lub firma\nWprowadz typ: ");
    String type = scanner.nextLine();
    
    while(!(type.contentEquals("firma") | type.contentEquals("osoba prywatna"))) {
      System.out.println("Nieznany typ\nWprowadz poprawny typ: ");
      type = scanner.nextLine();
    }
    
    if(type.contentEquals("firma")) {
      ArrayList<String> issuerData = insertCompanyData(scanner);
      controller.addBuyer(issuerData.get(0), Integer.parseInt(issuerData.get(1)), issuerData.get(2));
    }
    
    else {
      insertPersonData(scanner);
    }
    
    System.out.println("Dane wystawiajacego:");
    ArrayList<String> issuerData = insertCompanyData(scanner);
    controller.addIssuer(issuerData.get(0), Integer.parseInt(issuerData.get(1)), issuerData.get(2));
  
    String currentOperation = "go";
    while(!currentOperation.contentEquals("wyjdz")) {
      System.out.println(
              "dodaj - dodaje element do faktury\n"
                      + "wyswietl - wyswietla cala fakture\n"
                      + "wyjdz - wychodzi z programu\n");
      System.out.println("Wprowadz nazwe operacji: ");
      currentOperation = scanner.nextLine();
      if(currentOperation.contentEquals("dodaj")) {
        insertElementData(scanner);
      }
      else if (currentOperation.contentEquals("wyswietl")) {
        display();
      }
      else if (currentOperation.contentEquals("wyjdz")) {
        break;
      }
      else {
        System.out.println("Bledne dane, wprowadz operacje jeszcze raz.");
      }
    }
  }
}
