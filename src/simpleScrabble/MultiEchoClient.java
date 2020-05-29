package simpleScrabble;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.json.JSONArray;
import org.json.JSONObject;

public class MultiEchoClient extends Thread {

    private static int PORT = 1234;
    private static InetAddress host;
    public static boolean isOutputTime = false;
    Socket socket = null;
    private String message = "";
    public String username = "";
    JPanel2 j2;
    JPanel3 j3;
    Boolean isServerClient;
    public JSONArray gelenHamle;
    static int sayac = 0;
    JSONArray kurallar;

    JFrame myFrame;

    public MultiEchoClient(String host, int port, String kullanıcı_adı, Boolean isServerClient) {
        try {
            this.host = InetAddress.getByName(host);
        } catch (UnknownHostException ex) {
            Logger.getLogger(MultiEchoClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.PORT = port;
        try {
            socket = new Socket(host, PORT);
            this.username = kullanıcı_adı;
        } catch (IOException ex) {
            Logger.getLogger(MultiEchoClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.isServerClient = isServerClient;
    }

    public void run() {
        //System.out.println("Run çalıştı");
        JSONObject j = new JSONObject();
        j.put("baglanti_istegi", new JSONObject().put("nick_name", this.username).toString()).toString();
        this.sendMessages(j.toString());

        String server_response = this.getInput();
        //System.out.println("server :" + server_response);
        JSONObject gelenJSON = new JSONObject(server_response);
        String key = gelenJSON.keys().next();
        String value = gelenJSON.getString(key);

        if (key.equals("kurallar")) {
            //SET kurallar

            //eski windowu kapat
            myFrame = new JFrame("Oyun Hazırlık Aşaması");
            myFrame.setSize(600, 600);
            myFrame.setLocation(300, 100);

            JSONArray yeniJSON = new JSONArray(value);
            //System.out.println(yeniJSON.get(0));
            kurallar = yeniJSON;
            //System.out.println(yeniJSON.get(0));
            String x_boyut = yeniJSON.getJSONObject(0).getString("x_boyut");
            String y_boyut = yeniJSON.getJSONObject(1).getString("y_boyut");
            String kullanilmaz_hucre_sayisi = yeniJSON.getJSONObject(2).getString("kullanilmaz_hucre_sayisi");
            String x2_puan_hucre_sayisi = yeniJSON.getJSONObject(3).getString("x2_puan_hucre_sayisi");
            String x3_puan_hucre_sayisi = yeniJSON.getJSONObject(4).getString("x3_puan_hucre_sayisi");
            String kazanma_puan = yeniJSON.getJSONObject(5).getString("kazanma_puan");
            String toplam_puan = yeniJSON.getJSONObject(6).getString("toplam_puan");

            JSONObject ok = new JSONObject();
            ok.put("kurallar_ok", "");

            this.sendMessages(ok.toString());
            String in = this.getInput();
            JSONObject jobj = new JSONObject(in);

            j2 = new JPanel2(x_boyut, y_boyut, kullanilmaz_hucre_sayisi, x2_puan_hucre_sayisi, x3_puan_hucre_sayisi, kazanma_puan, toplam_puan, this);
            if (!isServerClient) {
                j2.buttonVisibility(false);
            }
            //System.out.println("kurallar set ediliyor");
            j2.setOyuncular(jobj);
            
            //System.out.println("oyuncular set ediliyor :"+jobj);
            myFrame.setContentPane(j2);

            myFrame.setVisible(true);
            myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        } else if (key.equals("game_availablity") && value.equals(false)) {
            //System.out.println("oyuna giris yapilamiyor");
        } else if (key.equals("empty_response") && value.equals("empty")) {
            //System.out.println("empty donmmus");
        }

        Client_Listen();
    }

    public void Client_Listen() {
        do {
            try {
                System.out.println("Client_Listen ");
                Scanner networkInput = new Scanner(socket.getInputStream());
                String gelen = networkInput.nextLine();
                System.out.println("Serverdan->cliente gelen :" + gelen);
                //return gelen;
                //System.out.println("hatali gelen    :" + gelen);
                JSONObject jsonObject = new JSONObject(gelen);
                System.out.println("CLIENT A GELEN:" + gelen);
                String key = jsonObject.keys().next();
                String value = jsonObject.get(key).toString();
                //System.out.println("GELEN KEY : " +key);
                //System.out.println("GELEN VALUE : " +value);
                if (key.equals("oyuncu_listesi")) {
                    //System.out.println("iffffffffffffffffffffffffffffffffffffffffffffffffffffffff");
                    this.j2.setOyuncular(jsonObject);

                }else if (key.equals("oyuna_basla")) {
                    System.out.println("sayac" + sayac);
                    sayac++;
                    System.out.println("cliente gelmis mi   :" + value);
                    JSONObject eklenenler = new JSONObject(value);
                    //System.out.println("hata burada mi1");
                    JSONArray jarray = eklenenler.getJSONArray("eklenenler");
                    gelenHamle = jarray;
                    
                    
                    
                    //System.out.println("hata burada mi2");

                    /*
                    for(int i = 0; i<jarray.length(); i++){
                        JSONObject koor = jarray.getJSONObject(i);
                        System.out.println("x ekseni:" + koor.getInt("x_ekseni"));
                        System.out.println("y ekseni:" + koor.getInt("y_ekseni"));
                        System.out.println("carpi:" + koor.getInt("carpi"));
                        System.out.println("----------------------------------");
                    }
                     */
                    this.j2.setVisible(false);
                    JFrame myFrame1 = new JFrame("Oyun Hazırlık Aşaması");
                    myFrame1.setSize(700, 750);
                    myFrame1.setLocation(300, 100);
                    //System.out.println("burdayiz");
                    j3 = new JPanel3(j2.x_boyut, j2.y_boyut, this);
                    j3.gelenHamle = jarray;
                    j3.ekraniAyarla(gelenHamle, j3.jtextArray);
                    j3.setOyuncular(j2.oyuncular);
                    myFrame1.setContentPane(j3);

                    //j3.listeleriAyarla();
                    
                    
                    myFrame1.setVisible(true);
                    this.myFrame.setVisible(false);

                    myFrame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    
                    //AHMETE YAPTIR
                    JSONObject ok = new JSONObject();
                    ok.put("oyuna_basladim", "");
                    this.sendMessages(ok.toString());

                }else if (key.equals("senin_siran") ) {
                    System.out.println(username + "sirasi");
                    JSONObject donus = new JSONObject();
                    //donus.put("sirami_aldim", value);

                    //this.sendMessages(donus.toString());
                    j3.setButtonVisibility(true);
                    System.out.println("true yaptik");
                    
                    JSONObject ok = new JSONObject();
                    ok.put("siram_geldi", "");
                    this.sendMessages(ok.toString());
                    
                }else if(key.equals("yeni_hamle_geldi")){
                    System.out.println("EKRANA BASILMIYOR BOZULMUŞMU"+value);
                    j3.ekraniAyarla(new JSONArray(value), j3.jtextArray);
                }
                else if(key.equals("kullanici_hata_yapti")){
                    
                    System.out.println("EKRANA BASILMIYOR BOZULMUŞMU"+value);
                    //j3.ekraniAyarla(new JSONArray(value), j3.jtextArray);
                    j3.HataMesaji(value+" oyuncusu hatalı hamle yaptı");
                    if (value.equals(this.username)) {
                        j3.setButtonVisibility(true);
                    }
                }
                
                else if(key.equals("veri")){
                    JSONObject jj = new JSONObject();
                    jj.put("veri", value);
                    j3.listeleriAyarla(jj);
                    
                     JSONObject ok = new JSONObject();
                    ok.put("veri_geldi", "");
                    this.sendMessages(ok.toString());
                }
                else {
                    System.out.println("else dusmus");
                    //System.out.println("if çalışmadı |"+key+"|"+key.length()+"|"+key.getClass().getName()+"| eşit değil |oyuncu_listesi|");
                }

            } catch (IOException ex) {
                Logger.getLogger(MultiEchoClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            //System.out.println("Serverdan->cliente gelen :-------------------");
        } while (true);

    }

    public String getInput() {
        try {
            Scanner networkInput = new Scanner(socket.getInputStream());
            String gelen = networkInput.nextLine();
            System.out.println("Serverdan->cliente gelen :" + gelen);
            return gelen;
        } catch (IOException ex) {
            Logger.getLogger(MultiEchoClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "bunu donuyor";
    }

    public void sendMessages(String message) {
        if (message == null) {
            message = "mesaj null imis ";
        }
        PrintWriter networkOutput = null;
        //System.out.println(socket);
        try {
            networkOutput = new PrintWriter(
                    socket.getOutputStream(), true);
        } catch (IOException ex) {
            Logger.getLogger(MultiEchoClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Set up stream for keyboard entry...
        if (networkOutput == null) {
            //System.out.println("network output null");
            return;
        }

        //System.out.println("clientdan servere son yollanacak yer: " + message);
        System.out.println("client -> server:" + message);
        networkOutput.println(message);

    }
}
