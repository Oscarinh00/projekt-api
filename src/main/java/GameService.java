import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.IOException;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class GameService {
    private static String id;
    private static int players;
    private static ArrayList fullCardList= new ArrayList<>();

    public static void Start() {

        Scanner ask = new Scanner(System.in);
        System.out.println("zaczynamy grę!");
        System.out.println("zasady:");
        System.out.println("- gracze mają wspólną talię składającą się z tylu talii, ilu jest graczy.");
        System.out.println("- karty od 2 do 10 mają wartość równą swojej wartości, walet to 2 punkty, dama to 3 punkty, król to 4 punkty, a as to 11 punktów");
        System.out.println("- osoba, która jest bliżej 21 punktów wygrywa");
        System.out.println("- jeżeli ktoś zdobędzie równo 21 punktów wygrywa natychmiastowo");
        System.out.println("to tyle. życzę miłej zabawy!");
        System.out.println("podaj liczbę osób grających:");
        String deck_count = ask.nextLine();

        players = parseInt(deck_count);

        String requestUrl = "https://deckofcardsapi.com/api/deck/new/shuffle/?deck_count=" + deck_count;


        try {

            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException(("HttpResponseCode: " + responseCode));
            } else {
                String inline = "";
                Scanner scanner = new Scanner(url.openStream());
                while (scanner.hasNext()) {
                    inline += scanner.nextLine();
                }
                scanner.close();
                JSONParser parser = new JSONParser();
                JSONObject data_obj = (JSONObject) parser.parse(inline);
                id = (String) data_obj.get("deck_id");
            }

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void Rozgrywka() {
        int tura = 1;
        while(tura <= players) {
            Scanner ask = new Scanner(System.in);
            System.out.println("ile chcesz kart?");
            String card_count = ask.nextLine();
            int check = parseInt(card_count);

            while (check != 0) {

                String requestUrl = "https://deckofcardsapi.com/api/deck/" + id + "/draw/?count=" + card_count;
                ArrayList cardList = new ArrayList<>();

                try {

                    URL url = new URL(requestUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode != 200) {
                        throw new RuntimeException(("HttpResponseCode: " + responseCode));
                    } else {
                        String inline = "";
                        Scanner scanner = new Scanner(url.openStream());
                        while (scanner.hasNext()) {
                            inline += scanner.nextLine();
                        }
                        scanner.close();
                        JSONParser parser = new JSONParser();
                        JSONObject data_obj = (JSONObject) parser.parse(inline);
                        List tempList = (List) data_obj.get("cards");
                        int size = tempList.size();
                        int i = 0;
                        while(i < size){
                            cardList.add(tempList.get(i));
                            System.out.println("twoja karta to: "+ tempList.get(i));
                            i++;
                        }

                        System.out.println("");
                        System.out.println("Co dalej? dobierasz kartę(1), czy kończysz turę(0)?");
                        card_count = ask.nextLine();
                        check = parseInt(card_count);
                        if (check == 0) {
                            fullCardList.add(cardList);
                            tura = tura + 1;
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        GameInfo gra = new GameInfo(players,fullCardList);

        System.out.println("podsumowanie gry:");
        System.out.println("liczba graczy: "+ gra.getPlayers());
        System.out.println("karty graczy: "+ gra.getCards());
    }
}