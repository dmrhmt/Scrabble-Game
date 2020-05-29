package simpleScrabble;

import java.io.*;
import static java.lang.Integer.parseInt;
import java.net.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;
import org.json.JSONArray;

public class MultiEchoServer extends Thread {

    private static ServerSocket serverSocket;
    public int PORT = 1234;
    private static ClientHandler clients[] = new ClientHandler[10];
    public JSONArray kurallar = new JSONArray();
    ClientHandler handler;

    private static boolean emptyClientSpace(ClientHandler[] clients) {
        for (int i = 0; i < clients.length; i++) {
            if (clients[i] == null) {
                return true;
            }
        }
        return false;
    }

    public void run() {
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException ioEx) {
            System.out.println("\nUnable to set up port!");
            System.exit(1);
        }

        for (int i = 0; i < clients.length; i++) {
            clients[i] = null;
        }

        int i = 0;
        do {
            //Wait for client...
            Socket client = null;
            try {
                client = serverSocket.accept();
            } catch (IOException ex) {
                Logger.getLogger(MultiEchoServer.class.getName()).log(Level.SEVERE, null, ex);
            }

            System.out.println("\nNew client accepted.\n");

            //Create a thread to handle communication with
            //this client and pass the constructor for this
            //thread a reference to the relevant socket...
            handler = new ClientHandler(client);
            handler.kurallar = kurallar;

            handler.start();//As usual, this method calls run.
            clients[i] = handler;

            handler.clients = clients;

            /*
            if(i==2){
                //BURADA DıKKAT ET! ASAGIDA CAGIRDIGIN CLIENT, DAHA DA ASAGIDA START OLMUS OLMALI KI BU KOD CALISSIN
                for(int l = 0; l<clients.length; l++)
                    clients[l].doOutput(l + "ye mesajimiz var");
            }
             */
            i++;
        } while (true);
    }
}

class ClientHandler extends Thread {

    private Socket client;
    private Scanner input;
    private PrintWriter output;
    public static String[] kullanicilar = new String[10];
    public static int[] kullanicilar_skor = new int[10];
    public JSONArray kurallar = new JSONArray();
    public ClientHandler clients[] = new ClientHandler[10];
    JSONObject oyuncular = new JSONObject();
    JSONObject anaJSON = new JSONObject();
    static int null_sayac = 0;
    public static String[] hazirlarMi = new String[10];
    static int oyunSirasi = 0;
    static JSONObject ekran = null;
    static int kontrol = 0;
    static int oyunSayac = 0;
    static int gercekKisi = 0;
    static int x;
    static int y;
    static JSONArray skor_bilgileri = new JSONArray();
    JSONObject veri = null;
    JSONObject donen = null;
    static boolean sira_degisimi = false;
    static boolean ilk_sirada_hamle_yapilmaz = false;
    static String value_bozulmayan = "";
    static int furkanBaba = 0;

    public ClientHandler(Socket socket) {
        //Set up reference to associated socket...
        client = socket;
        if (null_sayac == 0) {
            for (int i = 0; i < kullanicilar.length; i++) {
                kullanicilar[i] = null;
            }
        }
        if (null_sayac == 0) {
            for (int i = 0; i < hazirlarMi.length; i++) {
                hazirlarMi[i] = null;
            }
        }
        null_sayac++;
        try {
            input = new Scanner(client.getInputStream());
            output = new PrintWriter(
                    client.getOutputStream(), true);
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
    }

    private static boolean isGameFull() {
        for (int i = 0; i < kullanicilar.length; i++) {
            if (kullanicilar[i] == null) {
                return false;
            }
        }
        return true;
    }

    private static boolean isNicknameValid(String nickname) {
        for (int i = 0; i < kullanicilar.length; i++) {
            if (nickname.equals(kullanicilar[i]) || kullanicilar.equals("")) {
                return false;
            }
        }
        return true;
    }

    public void doOutput(JSONObject outputMes) {
        System.out.println("Server yolluyor :" + outputMes);
        output.println(outputMes);
    }

    public void haziraEkle(String name, String[] liste) {
        for (int i = 0; i < liste.length; i++) {
            if (name.equals(liste[i])) {
                return;
            }
        }
        for (int i = 0; i < liste.length; i++) {
            if (liste[i] == null) {
                liste[i] = name;
                return;
            }
        }
    }

    public JSONObject ekraniOlustur(int kullanilmaz, int x2, int x3, int x, int y) {

        List eklenenler = new ArrayList();
        int x1 = ((x * y) - (kullanilmaz + x2 + x3));
        Random randX = new Random();
        Random randY = new Random();
        JSONArray genel = new JSONArray();

        for (int i = 0; i < 3; i++) {
            JSONObject koor = new JSONObject();
            int randomX = (x / 2);
            int randomY = (y / 2) + i;
            if (!eklenenler.contains(randomX + "-" + randomY)) {
                koor.put("x_boyut", randomX);
                koor.put("y_boyut", randomY);
                koor.put("carpi", 1);
                if (i == 0) {
                    koor.put("deger", "t");
                } else if (i == 1) {
                    koor.put("deger", "e");
                } else {
                    koor.put("deger", "a");
                }
                koor.put("change", 0);
                genel.put(koor);
                eklenenler.add(randomX + "-" + randomY);
            } else {
                //kullanilmaz++;
            }
        }

        for (int i = 0; i < kullanilmaz; i++) {
            JSONObject koor = new JSONObject();
            int randomX = randX.nextInt(x);
            int randomY = randY.nextInt(y);
            if (!eklenenler.contains(randomX + "-" + randomY)) {
                koor.put("x_boyut", randomX);
                koor.put("y_boyut", randomY);
                koor.put("carpi", 0);
                koor.put("deger", "");
                koor.put("change", 0);
                genel.put(koor);
                eklenenler.add(randomX + "-" + randomY);
            } else {
                kullanilmaz++;
            }
        }

        for (int i = 0; i < x2; i++) {
            JSONObject koor = new JSONObject();
            int randomX = randX.nextInt(x);
            int randomY = randY.nextInt(y);
            if (!eklenenler.contains(randomX + "-" + randomY)) {
                koor.put("x_boyut", randomX);
                koor.put("y_boyut", randomY);
                koor.put("carpi", 2);
                koor.put("deger", "");
                koor.put("change", 0);
                genel.put(koor);
                eklenenler.add(randomX + "-" + randomY);
            } else {
                x2++;
            }
        }

        for (int i = 0; i < x3; i++) {
            JSONObject koor = new JSONObject();
            int randomX = randX.nextInt(x);
            int randomY = randY.nextInt(y);
            if (!eklenenler.contains(randomX + "-" + randomY)) {
                koor.put("x_boyut", randomX);
                koor.put("y_boyut", randomY);
                koor.put("carpi", 3);
                koor.put("deger", "");
                koor.put("change", 0);
                genel.put(koor);
                eklenenler.add(randomX + "-" + randomY);
            } else {
                x3++;
            }
        }
        for (int i = 0; i < x1 - 3; i++) {
            JSONObject koor = new JSONObject();
            int randomX = randX.nextInt(x);
            int randomY = randY.nextInt(y);
            if (!eklenenler.contains(randomX + "-" + randomY)) {
                koor.put("x_boyut", randomX);
                koor.put("y_boyut", randomY);
                koor.put("carpi", 1);
                koor.put("deger", "");
                koor.put("change", 0);
                genel.put(koor);
                eklenenler.add(randomX + "-" + randomY);
            } else {
                x1++;
            }
        }
        //System.out.println("genel boyutu:      " + genel.length());
        JSONObject yollanacak = new JSONObject();
        yollanacak.put("eklenenler", genel);
        //System.out.println("yollaanananackk:  " + yollanacak);
        return yollanacak;

    }

    private JSONObject kararFonksiyonu(JSONObject gelen) {
        //System.out.println("karar fonk json object:" + gelen);
        String key = gelen.keys().next();
        //System.out.println("karar fonk key: " + key);
        String value = gelen.getString(key);

        if (key.equals("kurallar_ok")) {
            for (int j = 0; j < kullanicilar.length; j++) {
                if (kullanicilar[j] != null) {
                    oyuncular.put("oyuncu" + String.valueOf(j), kullanicilar[j]);
                }
            }
            //System.out.println("ouyncularrrrrr:" + oyuncular.toString());
            anaJSON.put("oyuncu_listesi", oyuncular.toString());
            return anaJSON;
        }

        if (key.equals("baglanti_istegi")) {
            JSONObject nickJson = new JSONObject(value);
            String nickname = nickJson.getString("nick_name");
            if (isNicknameValid(nickname) && !isGameFull()) {
                for (int i = 0; i < kullanicilar.length; i++) {
                    if (kullanicilar[i] == null) {
                        kullanicilar[i] = nickname;
                        kullanicilar_skor[i] = 0;

                        JSONObject birObje = new JSONObject();
                        birObje.put("oyuncu", nickname);
                        birObje.put("puan", 0);
                        birObje.put("skor", 0);
                        skor_bilgileri.put(birObje);
                        break;
                    }
                }
                JSONObject kuralObject = new JSONObject();
                kuralObject.put("kurallar", kurallar.toString());

                return kuralObject;
            }
            JSONObject notAccept = new JSONObject();
            notAccept.put("game_availablity", "false");
            return notAccept;
        }
        if (key.equals("oyun_baslatildi")) {
            //System.out.println("OYUN BASLATILDI MESJI -----------------------------");
            int x_boyut = kurallar.getJSONObject(0).getInt("x_boyut");
            int y_boyut = kurallar.getJSONObject(1).getInt("y_boyut");
            this.x = x_boyut;
            this.y = y_boyut;
            int kullanilmaz = kurallar.getJSONObject(2).getInt("kullanilmaz_hucre_sayisi");
            int x2 = kurallar.getJSONObject(3).getInt("x2_puan_hucre_sayisi");
            int x3 = kurallar.getJSONObject(4).getInt("x3_puan_hucre_sayisi");

            JSONObject oyunBasla = new JSONObject();
            if (ekran == null) {
                ekran = ekraniOlustur(kullanilmaz, x2, x3, x_boyut, y_boyut);
            }
            oyunBasla.put("oyuna_basla", ekran);
            //System.out.println("oyunSayac" + oyunSayac);
            oyunSayac++;
            return oyunBasla;
        }

        JSONObject empty = new JSONObject();
        empty.put("empty_response", "empty");
        return empty;

    }

    public JSONObject kullaniciInputKontrol(int x_boyut, int y_boyut, JSONObject hamlejson) throws IOException {
        //System.out.println("sana gelen json:" + hamlejson);
        //System.out.println("GELEN input :" + hamlejson);
        String key = hamlejson.keys().next();
        //System.out.println("CLIENT A GELEN KEY :" + key);

        String value = hamlejson.getString(key);
        //System.out.println("CLIENT A GELEN VALUE :" + value);

        JSONArray harflerjson = new JSONArray(value);
        //hamle kontrol sol-yukari

        Boolean bulundu = false;
        Boolean soldan_sag_calistimi = false;
        int bulunan_length = 0;
        ArrayList<String> myList = new ArrayList();
        ArrayList<String> myList_carpi_bilgisi = new ArrayList();
        ArrayList<Integer> myList_kordinat_i = new ArrayList();
        //System.out.println("harflerjson:" + harflerjson.length());
        for (int i = 0; i < harflerjson.length(); i++) {
            JSONObject harf = harflerjson.getJSONObject(i);
            /*
            System.err.println("-----------------------------------");
            System.out.println("harf: " + harf);
            System.out.println("hangi harf :" + harf.getString("deger"));
            System.out.println("x_ekseni  :" + harf.getInt("x_ekseni"));
            System.out.println("y_ekseni  :" + harf.getInt("y_ekseni"));
            System.out.println("carpi kac :" + harf.getInt("carpi"));
            System.out.println("change :" + harf.getInt("change"));
             */
            //System.out.println("i:" + i);
            if (harf.getString("change").equals("true") && (!soldan_sag_calistimi)) {//soldan -saga kelime
                //System.out.println("soldan saga var mi?");
                for (int j = i; j < harflerjson.length(); j++) {
                    JSONObject harf2 = harflerjson.getJSONObject(j);
                    if (harf2.getString("change").equals("true")) {
                        //System.out.println("deger :" + harf2.getString("deger"));
                        myList.add(harf2.getString("deger"));
                        myList_carpi_bilgisi.add(Integer.toString(harf2.getInt("carpi")));
                        myList_kordinat_i.add(j);
                        bulunan_length++;
                        soldan_sag_calistimi = true;
                        if (bulunan_length == 2) {
                            bulundu = true;
                            System.out.println("soldan saga bulundu");
                        }

                    } else if (bulunan_length == 1) {
                        //System.out.println("soldan saga bulunamadi");
                        soldan_sag_calistimi = false;
                        bulundu = false;
                        myList.clear();
                        myList_carpi_bilgisi.clear();
                        myList_kordinat_i.clear();
                        break;
                    }
                }

            }
            //System.out.println("ikici if?");
            if (harf.getString("change").equals("true") && (!bulundu)) {
                //System.out.println("yukari asagi var mi ?");
                for (int j = i; j < harflerjson.length(); j = j + y_boyut) {
                    //System.out.println("yukaridan asagi var j ->" + j);
                    JSONObject harf2 = harflerjson.getJSONObject(j);
                    if (harf2.getString("change").equals("true")) {
                        //System.out.println("deger :" + harf2.getString("deger"));
                        myList.add(harf2.getString("deger").toString());
                        myList_carpi_bilgisi.add(Integer.toString(harf2.getInt("carpi")));
                        myList_kordinat_i.add(j);
                    } else {
                        break;
                    }
                }
                break;
            }
        }
        //System.out.println("Sonuc :");
        for (int i = 0; i < myList.size(); i++) {
            //System.out.println("input " + myList.get(i));
            //System.out.println("input degeri " + myList_carpi_bilgisi.get(i));
            //System.out.println("indeks degeri " + myList_kordinat_i.get(i));

        }

        ArrayList<String> yukariList = new ArrayList();
        ArrayList<String> asagiList = new ArrayList();
        ArrayList<String> solList = new ArrayList();
        ArrayList<String> sagList = new ArrayList();

        if (soldan_sag_calistimi) {

            //SOLA BAK
            for (int i = myList_kordinat_i.get(0) - 1; (y_boyut - 1) != (i % y_boyut) && i != -1; i--) {

                JSONObject harf = harflerjson.getJSONObject(i);
                //System.out.println(harf.getString("deger"));
                if (!harf.getString("deger").equals("")) {
                    solList.add(harf.getString("deger"));
                } else {
                    break;
                }
            }

            Collections.reverse(solList);
            System.out.println("sol listem " + solList.toString());

            //SAĞA BAK
            for (int i = (myList_kordinat_i.get(myList_kordinat_i.size() - 1)) + 1; 0 != (i % y_boyut); i++) {

                JSONObject harf = harflerjson.getJSONObject(i);
                //System.out.println(harf.getString("deger"));
                if (!harf.getString("deger").equals("")) {
                    sagList.add(harf.getString("deger"));
                } else {
                    break;
                }
            }

            System.out.println("sag listem " + sagList.toString());
            //myList.addAll(solList);

            solList.addAll(myList);
            solList.addAll(sagList);

        } else {

            //YUKARI BAK
            for (int i = (myList_kordinat_i.get(0)) - y_boyut; 0 < i; i = i - y_boyut) {

                JSONObject harf = harflerjson.getJSONObject(i);
                //System.out.println(harf.getString("deger"));
                if (!harf.getString("deger").equals("")) {
                    yukariList.add(harf.getString("deger"));
                } else {
                    break;
                }
            }

            Collections.reverse(yukariList);
            System.out.println("yukaru listem " + yukariList.toString());

            //ASAĞI BAK
            for (int i = (myList_kordinat_i.get(myList_kordinat_i.size() - 1)) + y_boyut; i < x_boyut * y_boyut; i = i + y_boyut) {

                JSONObject harf = harflerjson.getJSONObject(i);
                //System.out.println(harf.getString("deger"));
                if (!harf.getString("deger").equals("")) {
                    asagiList.add(harf.getString("deger"));
                } else {
                    break;
                }
            }

            System.out.println("asagı listem " + asagiList.toString());
            yukariList.addAll(myList);
            yukariList.addAll(asagiList);
        }

        BufferedReader bufReader = null;
        try {

            bufReader = new BufferedReader(new FileReader("C:\\Users\\demir\\OneDrive\\Belgeler\\NetBeansProjects\\JavaApplication5\\src\\javaapplication5\\dictionary.txt"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        ArrayList<String> listOfLines = new ArrayList<>();
        String line = bufReader.readLine();
        while (line != null) {
            listOfLines.add(line);
            line = bufReader.readLine();
        }
        bufReader.close();

        String input_string = "";

        if (soldan_sag_calistimi) {
            input_string = solList.stream().map((s) -> s).reduce(input_string, String::concat);

        } else {
            input_string = yukariList.stream().map((s) -> s).reduce(input_string, String::concat);

        }
        System.out.println("DEĞİŞİKLİK FURKAN : " + input_string);
        if (listOfLines.contains(input_string)) {
            System.out.println("Hocanin listesinde var");

            //---------------------------------------------------------------------
            //hocanin listesinde varsa puanini verme
            int kazanilan_puan = myList.size();
            for (int i = 0; i < myList.size(); i++) {
                //System.out.println("elemaninin :" + myList.get(i) + "| puani " + myList_carpi_bilgisi.get(i));
                kazanilan_puan = kazanilan_puan * parseInt(myList_carpi_bilgisi.get(i));
            }
            System.out.println("Kazanilan puan:" + kazanilan_puan);

            /*
            kullanicilar_skor[oyunSirasi]=kullanicilar_skor[oyunSirasi]+ kazanilan_puan;
            JSONObject birObje = new JSONObject();
            birObje.put("oyuncu", kullanicilar[oyunSirasi]);
            birObje.put("puan", kullanicilar_skor[oyunSirasi]);
            birObje.put("skor", 0);

            skor_bilgileri.put(birObje);
            veri = new JSONObject();
            veri.put("veri", skor_bilgileri.toString());
             */
            kullanicilar_skor[oyunSirasi] = kullanicilar_skor[oyunSirasi] + kazanilan_puan;
            JSONObject birObje = new JSONObject();
            birObje.put("oyuncu", kullanicilar[oyunSirasi]);
            birObje.put("puan", kullanicilar_skor[oyunSirasi]);
            birObje.put("skor", 0);

            for (int i = 0; i < skor_bilgileri.length(); i++) {
                if (skor_bilgileri.getJSONObject(i).getString("oyuncu").equals(kullanicilar[oyunSirasi])) {
                    skor_bilgileri.getJSONObject(i).put("puan", kullanicilar_skor[oyunSirasi]);
                    skor_bilgileri.getJSONObject(i).put("skor", 1);
                } else {
                    //skor_bilgileri.put(birObje);
                }
            }
            veri = new JSONObject();
            veri.put("veri", skor_bilgileri.toString());

        } else {
            veri = null;
        }
        return veri;
    }

    @Override
    public void run() {
        String received = "";
        int o = 0;
        if (input.hasNext()) {
            do {
                //Accept message from client on
                //the socket's input stream...

                received = input.nextLine();

                //Echo message back to client on
                //the socket's output stream...
                //BURADA GELEN received STRINGINI BELKI METOD VEYA SINIFA YOLLAYABILIRIZ
                //AYNI ZAMANDA OUTPUT OLARAK DA YAZILACAK SEYI O SINIFTAN DONDURMELIYIZ
                //output.println("ECHO: " + received);
                //Repeat above until 'QUIT' sent by client...
                System.out.println("Server okudu :" + received);
                JSONObject jsonObject = new JSONObject(received);
                //System.out.println("json objectimiz " + jsonObject);
                //System.out.println("jsonobject key:" + key);
                //System.out.println("jsonobject value:" + value);
                //System.out.println("jsonobject key set" + jsonObject.keySet());
                String key = jsonObject.keys().next();
                String value = jsonObject.getString(key);

                if (key.equals("kurallar_ok")) {
                    //System.out.println("control" + String.valueOf(o));
                    o++;
                    for (int i = 0; i < clients.length; i++) {
                        if (clients[i] != null) {
                            clients[i].doOutput(kararFonksiyonu(jsonObject));
                        }
                    }
                } else if (/*jsonObject.keys().next().equals("oyun_baslatildi")*/key.equals("oyun_baslatildi")) {
                    for (int i = 0; i < clients.length; i++) {
                        if (clients[i] != null) {
                            clients[i].doOutput(kararFonksiyonu(jsonObject));
                            //System.out.println("client remoteları" + clients[i].client.getRemoteSocketAddress());
                        }
                    }
                } //AHMETE YAPTIR
                else if (key.equals("oyuna_basladim")) {
                    //System.out.println("oyuna BASLAMIS:     " + kontrol);
                    kontrol++;
                    JSONObject seninSiran = new JSONObject();
                    seninSiran.put("senin_siran", jsonObject.getString(key));
                    //System.out.println("ECHO : sira yollama mesajı içindeyim" + this.client);
                    //System.out.println("ECHO : array clienti" + clients[oyunSirasi].client.getRemoteSocketAddress());
                    if (this.client.equals(clients[oyunSirasi].client)) {
                        //System.out.println("ikinci if");
                        System.out.println("server 0'a yolluyormu ?" + seninSiran);
                        clients[0].doOutput(seninSiran);
                        //System.out.println("senin sirani yazdirdim" + seninSiran);

                        for (int i = 0; i < clients.length; i++) {
                            if (clients[i] != null) {
                                gercekKisi++;
                            }
                        }
                        oyunSirasi = oyunSirasi % gercekKisi;
                    }
                    oyunSirasi = 0;

                } else if (key.equals("hamle_yaptim")) {
                    ilk_sirada_hamle_yapilmaz = true;
                    value_bozulmayan = value;
                    try {
                        //PUAN HESAPLAMASI YAPIP HERKESE YOLLANACAK !!!!!!!!
                        //FURKAN YAPACAK

                        donen = kullaniciInputKontrol(x, y, jsonObject);
                    } catch (IOException ex) {
                        Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (donen == null) {
                        System.out.println("donen null geldi");

                        for (int i = 0; i < clients.length; i++) {
                            if (clients[i] != null) {
                                JSONObject gidecek = new JSONObject();
                                gidecek.put("kullanici_hata_yapti", kullanicilar[oyunSirasi]);
                                clients[i].doOutput(gidecek);
                            }
                        }

                    } else {
                        sira_degisimi = true;
                        for (int i = 0; i < clients.length; i++) {
                            if (clients[i] != null) {
                                clients[i].doOutput(donen);
                                System.out.println("donen geldi yolluyorum");

                            }
                        }

                    }
                } else if (key.equals("veri_geldi")/* && clients[oyunSirasi + 1].client.equals(this.client)*/) {
                    furkanBaba++;
                    if (furkanBaba % 3 == 0) {
                        JSONObject seninSiran = new JSONObject();
                        seninSiran.put("senin_siran", "");
                        oyunSirasi++;
                        //System.out.println("oyunsirasi1 :" + oyunSirasi);
                        oyunSirasi = (oyunSirasi % (gercekKisi));
                        //System.out.println("oyunsirasi2 :" + oyunSirasi);
                        //System.out.println("GERCEK KISI" + (gercekKisi));
                        //System.out.println("oyunSIRASI:" + oyunSirasi);
                        clients[oyunSirasi].doOutput(seninSiran);

                    }

                } else if (key.equals("siram_geldi") && ilk_sirada_hamle_yapilmaz) {
                    System.out.println("olacak dedik!" + value_bozulmayan); // ARRAY
                    for (int i = 0; i < clients.length; i++) {
                        if (clients[i] != null) {
                            JSONObject gidecek = new JSONObject();
                            gidecek.put("yeni_hamle_geldi", value_bozulmayan);
                            clients[i].doOutput(gidecek);
                        }
                    }
                } else {
                    doOutput(kararFonksiyonu(jsonObject));
                    //System.out.println("control burasi");
                }
            } while (!received.equals("QUIT"));
        }

        try {
            if (client != null) {
                //System.out.println(
                // "Closing down connection...");
                client.close();
            }
        } catch (IOException ioEx) {
            //System.out.println("Unable to disconnect!");
        }
    }
}
