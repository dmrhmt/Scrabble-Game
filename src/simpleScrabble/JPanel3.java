/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simpleScrabble;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JTextField;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Random;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author mfyildirim97
 */
public class JPanel3 extends javax.swing.JPanel {

    MultiEchoClient mec;
    int x;
    int y;
    public JSONArray gelenHamle;
    JTextField[] jtextArray;
    public JSONObject oyuncular;

    /**
     * Creates new form JPanel3
     */
    public void setButtonVisibility(Boolean visibility) {
        this.jButton1.setVisible(visibility);
    }
    public void HataMesaji(String mesaj){
        //JOptionPane.showMessageDialog(null, "Ahmete ..");
        //JOptionPane.show
        JOptionPane optionPane = new JOptionPane(mesaj, JOptionPane.ERROR_MESSAGE);    
        JDialog dialog = optionPane.createDialog("Failure");
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);

    }
    public void setOyuncular(JSONObject oyuncular) {
        this.oyuncular = oyuncular;
        
        
        String value = oyuncular.getString(oyuncular.keys().next());
        JSONObject son = new JSONObject(value);
        String[] arr = new String[10];
        
        //System.out.println("uzunluk:::: "+son.length());
        for(int i = 0; i<son.length();i++){
            arr[i] = son.getString("oyuncu"+i);
            //System.out.println("keysss "+son.keys());
            //oyuncular.getString(value)
        }
        this.jList1.setListData(arr);
    }
    
    public void listeleriAyarla(JSONObject veri){
        //key = "veri"
        //value yeni jsonArray
        //jeni jsonobject key:"puan", value:10, key: "skor", value:3
        
        
        
        JSONArray jarr = new JSONArray(veri.getString("veri"));
        String[] oyuncuList = new String[jarr.length()];
        String[] skorList = new String[jarr.length()];
        String[] puanList = new String[jarr.length()];
        for(int i = 0; i<jarr.length(); i++){
            oyuncuList[i] = jarr.getJSONObject(i).getString("oyuncu");
             skorList[i] = String.valueOf(jarr.getJSONObject(i).getInt("skor"));
             puanList[i] = String.valueOf(jarr.getJSONObject(i).getInt("puan"));
        }
        jList1.setListData(oyuncuList);
        jList2.setListData(puanList);
        jList3.setListData(skorList);
    }
    
    public void ekraniAyarla(JSONArray gelen, JTextField[] jtextArray) {
        //BURADA GELENI PARSE ET
        gelenHamle = gelen;
        //System.out.println("ekrani ayarla gelen" + gelen.length());

        for (int i = 0; i < gelen.length(); i++) {
            JSONObject jobj = gelen.getJSONObject(i);
            int valueX = jobj.getInt("x_boyut");
            int valueY = jobj.getInt("y_boyut");
            String gelenString = jobj.getString("deger");
            
            switch (jobj.getInt("carpi")) {
                case 0:
                    //BURASI SIYAH YERLER
                    jtextArray[((valueX * (y)) + (valueY))].setText(gelenString);
                    jtextArray[((valueX * (y)) + (valueY))].setEditable(false);
                    jtextArray[((valueX * (y)) + (valueY))].setBackground(Color.black);
                    jtextArray[((valueX * (y)) + (valueY))].setColumns(5);
                    break;
                //System.out.println("i degeri " + i);
                case 1:
                    //BURASI GRI YERLER
                    jtextArray[((valueX * (y)) + (valueY))].setText(gelenString);
                    jtextArray[((valueX * (y)) + (valueY))].setBackground(Color.lightGray);
                    jtextArray[((valueX * (y)) + (valueY))].setForeground(Color.RED);
                    break;
                case 2:
                    //BURASI SARI YERLER
                    jtextArray[((valueX * (y)) + (valueY))].setText(gelenString);
                    jtextArray[((valueX * (y)) + (valueY))].setBackground(Color.yellow);
                    jtextArray[((valueX * (y)) + (valueY))].setForeground(Color.RED);
                    break;
                case 3:
                    //BURASI YESIL YERLER
                    jtextArray[((valueX * (y)) + (valueY))].setText(gelenString);
                    jtextArray[((valueX * (y)) + (valueY))].setBackground(Color.green);
                    jtextArray[((valueX * (y)) + (valueY))].setForeground(Color.RED);
                    break;
                default:
                    break;
            }
        }
        
        
        Integer.valueOf(mec.kurallar.getJSONObject(0).getString("x_boyut"));
        jTextField1.setEditable(false);
        jTextField1.setText(String.valueOf(x) + "X" + String.valueOf(y));
        
        jTextField2.setEditable(false);
        jTextField2.setText(mec.kurallar.getJSONObject(5).getString("kazanma_puan"));
        
        jTextField3.setEditable(false);
        jTextField3.setText(mec.kurallar.getJSONObject(2).getString("kullanilmaz_hucre_sayisi"));
        
        jTextField4.setEditable(false);
        jTextField4.setText(mec.kurallar.getJSONObject(6).getString("toplam_puan"));
        
        jTextField5.setEditable(false);
        jTextField5.setText(mec.kurallar.getJSONObject(3).getString("x2_puan_hucre_sayisi"));
        
        jTextField6.setEditable(false);
        jTextField6.setText(mec.kurallar.getJSONObject(4).getString("x3_puan_hucre_sayisi"));
        /*
        for(int k = 0; k<kullanilamaz.length(); k++){
            JSONObject jsono = kullanilamaz.getJSONObject(k);
            System.out.println("jsono:      " + jsono);
            int valueX = jsono.getInt("x_ekseni");
            System.out.println("valuex" + valueX);
            int valueY = jsono.getInt("y_ekseni");
            System.out.println("valuey" + valueY);
            for(int i = 0; i<x; i++)
                for(int j = 0; j<y; j++){
                    System.out.println("deger:" + ((valueX*x)+(valueX+valueY)));
                    jtextArray[((valueX*x)+(valueX+valueY))].setText("kullanilamaz"); 
                    jtextArray[((valueX*x)+(valueX+valueY))].setBackground(Color.black);
                }
        }
        /*
        for(int k = 0; k<sarilar.length(); k++){
            JSONObject jsono = sarilar.getJSONObject(k);
            System.out.println("sarilar:      " + jsono);
            int valueX = jsono.getInt("x");
            System.out.println("valuex" + valueX);
            int valueY = jsono.getInt("y");
            System.out.println("valuey" + valueY);
            for(int i = 0; i<x; i++)
                for(int j = 0; j<y; j++){
                    System.out.println("deger:" + ((valueX*x)+(valueX+valueY)));
                    jtextArray[((valueX*x)+(valueX+valueY))].setText("sarilar"); 
                    jtextArray[((valueX*x)+(valueX+valueY))].setBackground(Color.yellow);
                }
        }
        
        for(int k = 0; k<kirmizilar.length(); k++){
            JSONObject jsono = kirmizilar.getJSONObject(k);
            System.out.println("kirmizilar:      " + jsono);
            int valueX = jsono.getInt("x");
            System.out.println("valuex" + valueX);
            int valueY = jsono.getInt("y");
            System.out.println("valuey" + valueY);
            for(int i = 0; i<x; i++)
                for(int j = 0; j<y; j++){
                    System.out.println("deger:" + ((valueX*x)+(valueX+valueY)));
                    jtextArray[((valueX*x)+(valueX+valueY))].setText("kirmizilar"); 
                    jtextArray[((valueX*x)+(valueX+valueY))].setBackground(Color.green);
                }
        }
         */

    }

    public JPanel3(int x, int y, MultiEchoClient mec) {
        initComponents();
        this.mec = mec;
        this.x = x;
        this.y = y;
        Random rand = new Random();
        String[] specs = new String[x * y];
        //for(int i = 0; i<specs.length;i++)

        jtextArray = new JTextField[x * y];
        //System.out.println("x:" + x + "y:" + y);
        this.jPanel1.setLayout(new GridLayout(x, y));
        for (int i = 0; i < (x * y); i++) {
            JTextField jtext = new JTextField("" + i);
            jtext.setName("ahmet" + i);
            jtextArray[i] = jtext;
            this.jPanel1.add(jtext);
        }

        //ekraniAyarla(jtextArray);
        jButton1.setVisible(false);
        System.out.println("false yaptik");

        JSONObject hazirim = new JSONObject();
        hazirim.put("hazirim", mec.username);
        mec.sendMessages(hazirim.toString());

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList3 = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 306, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 251, Short.MAX_VALUE)
        );

        jButton1.setText("Yolla");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        jList2.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jList2);

        jList3.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(jList3);

        jLabel1.setText("Oyuncular");

        jLabel2.setText("Puan");

        jLabel3.setText("Skor");

        jLabel4.setBackground(new java.awt.Color(51, 51, 255));
        jLabel4.setText("Oyun Bilgileri");

        jLabel5.setBackground(new java.awt.Color(51, 51, 255));
        jLabel5.setText("Oyun Alanı");

        jLabel6.setBackground(new java.awt.Color(51, 51, 255));
        jLabel6.setText("Kullanılmaz Bölge Alanı");

        jLabel7.setBackground(new java.awt.Color(51, 51, 255));
        jLabel7.setText("Kazanma Puanı");

        jLabel8.setBackground(new java.awt.Color(51, 51, 255));
        jLabel8.setText("Toplam Oyun");

        jLabel9.setBackground(new java.awt.Color(51, 51, 255));
        jLabel9.setText("X2 Sayısı");

        jLabel10.setBackground(new java.awt.Color(51, 51, 255));
        jLabel10.setText("X3 Sayısı");

        jTextField1.setText("jTextField1");

        jTextField2.setText("jTextField1");

        jTextField3.setText("jTextField1");

        jTextField4.setText("jTextField1");

        jTextField5.setText("jTextField1");

        jTextField6.setText("jTextField1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel10)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(12, 12, 12)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jScrollPane1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton1)))
                        .addContainerGap(38, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(23, 23, 23))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jButton1)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        JSONArray yollanacakHamle = new JSONArray();
        //System.out.println("AHMET");
        for (int i = 0; i < jtextArray.length; i++) {
            JSONObject harfler = new JSONObject();
            harfler.put("deger", jtextArray[i].getText());

            int y_koordinat = (i % (y));
            //System.out.println("y koordinat:    " + y_koordinat);
            harfler.put("y_boyut", y_koordinat);

            int x_koordinat = (i / (x));

            //System.out.println("y koordinat:    " + y_koordinat);
            //System.out.println("x yuvarlaniyor mu:" + x_koordinat);

            harfler.put("x_boyut", x_koordinat);
            if (jtextArray[i].getBackground().equals(Color.black)) {
                harfler.put("carpi", 0);
            } else if (jtextArray[i].getBackground().equals(Color.yellow)) {
                harfler.put("carpi", 2);
            } else if (jtextArray[i].getBackground().equals(Color.green)) {
                harfler.put("carpi", 3);
            } else {
                harfler.put("carpi", 1);
            }
            // ILK DURUMDA GELEN HAMLE OLMAYACAK, ONU DA DUSUN!
            yollanacakHamle.put(harfler);
            //System.out.println("harflerr:" + harfler);
        }
        //System.out.println("gelenHamleLength:" + gelenHamle.length());
        //System.out.println("yollanacakHamleLength:" + yollanacakHamle.length());
        for (int p = 0; p < gelenHamle.length(); p++) {
            String gelenDeger = gelenHamle.getJSONObject(p).getString("deger");
            int gelenX = gelenHamle.getJSONObject(p).getInt("x_boyut");
            int gelenY = gelenHamle.getJSONObject(p).getInt("y_boyut");

            for (int f = 0; f < yollanacakHamle.length(); f++) {
                String yollanacakDeger = yollanacakHamle.getJSONObject(f).getString("deger");
                int yollanacakX = yollanacakHamle.getJSONObject(f).getInt("x_boyut");
                int yollanacakY = yollanacakHamle.getJSONObject(f).getInt("y_boyut");

                if (yollanacakX == gelenX && yollanacakY == gelenY) {
                    if (gelenDeger.equals(yollanacakDeger)) {
                        yollanacakHamle.getJSONObject(f).put("change", "false");
                        //System.out.println("CHANGE  EKLENDI" + p);
                        break;
                    } else {
                        yollanacakHamle.getJSONObject(f).put("change", "true");
                        //System.out.println("CHANGE  EKLENMEDI" + p);
                        break;
                    }
                }
            }
        }
        
        //System.out.println("hamleeeeeeeeeeeee:" + yollanacakHamle);

        JSONObject json = new JSONObject();
        json.put("hamle_yaptim", yollanacakHamle.toString());
        //System.out.println("hamle yaptim");
        mec.sendMessages(json.toString());
        this.jButton1.setVisible(false);

    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList<String> jList1;
    private javax.swing.JList<String> jList2;
    private javax.swing.JList<String> jList3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    // End of variables declaration//GEN-END:variables
}
