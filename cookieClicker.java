import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class CookieClicker extends JFrame {
    private int kurabiyeler = 0;
    private int tikBasinaKazanc = 1;
    private int saniyeBasinaKazanc = 0;
    private JLabel kurabiyeLabel;
    private JLabel kazancLabel;
    private JLabel talimatLabel;
    private JButton magazaButonu;
    private JButton ozellikButonu;
    private Timer otomatikTiklayici;
    private List<Yukseltme> ozellikler=new ArrayList<>();
    private List<Yukseltme> yukseltmeler = new ArrayList<>();
    private JPanel magazaPaneli;
    private ImageIcon kurabiyeResmi;
    private JPanel ozelliklerPaneli;
    private boolean aktif10xKazanc = false;
    private int kalan10xTiklama = 0;


    public CookieClicker() {
        super("🍪 Kurabiye Tıklayıcı");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLayout(new BorderLayout());

        kurabiyeResmiYukle();
        yukseltmeleriOlustur();
        anaPaneliOlustur();
        magazaPaneliOlustur();
        ozelliklerOlustur();
        ozellikPaneliolustur();

        otomatikTiklayiciBaslat();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void kurabiyeResmiYukle() {
        try {
            kurabiyeResmi = new ImageIcon(getClass().getResource("/cookie.png"));
            if (kurabiyeResmi.getIconWidth() == -1) {
                kurabiyeResmi = new ImageIcon("cookie.png");
            }
        } catch (Exception e) {
            kurabiyeResmi = varsayilanKurabiyeResmi();
        }
    }

    private void yukseltmeleriOlustur() {
        yukseltmeler.add(new Yukseltme("Daha İyi Fare", 50, 0, 1));
        yukseltmeler.add(new Yukseltme("Altın Fare", 200, 0, 5));
        yukseltmeler.add(new Yukseltme("Elmas Fare", 1000, 0, 10));
        yukseltmeler.add(new Yukseltme("Büyükanne", 100, 1, 0));
        yukseltmeler.add(new Yukseltme("Çiftlik", 500, 5, 0));
        yukseltmeler.add(new Yukseltme("Fabrika", 2000, 10, 0));
        yukseltmeler.add(new Yukseltme("Maden", 5000, 20, 0));
        yukseltmeler.add(new Yukseltme("Nakliye", 10000, 50, 0));
    }
    private void ozelliklerOlustur()
    {
        ozellikler.add(new Yukseltme("10x Kazanç (5 Tık)", 500, 0, 0));
        ozellikler.add(new Yukseltme("2x Kazanç (10sn)", 3, 0, 0));
        ozellikler.add(new Yukseltme("Otomatik Artış", 1000, 0, 0));
    }

    private void anaPaneliOlustur() {
        JPanel anaPanel = new JPanel();
        anaPanel.setLayout(new BoxLayout(anaPanel, BoxLayout.Y_AXIS));
        anaPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        kurabiyeLabel = new JLabel("Kurabiyeler: " + kurabiyeler, SwingConstants.CENTER);
        kurabiyeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        kurabiyeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        kazancLabel = new JLabel("Saniyede Kazanç: " + saniyeBasinaKazanc, SwingConstants.CENTER);
        kazancLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        kazancLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        talimatLabel = new JLabel("Kurabiye kazanmak için resme tıklayın", SwingConstants.CENTER);
        talimatLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        talimatLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton kurabiyeButonu = new JButton(new ImageIcon(
                kurabiyeResmi.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
        kurabiyeButonu.setBorder(BorderFactory.createEmptyBorder());
        kurabiyeButonu.setContentAreaFilled(false);
        kurabiyeButonu.setAlignmentX(Component.CENTER_ALIGNMENT);
        kurabiyeButonu.addActionListener(e -> kurabiyeTikla());

        magazaButonu = new JButton("🛒 Mağazayı Aç");
        magazaButonu.setFont(new Font("Arial", Font.BOLD, 14));
        magazaButonu.setAlignmentX(Component.CENTER_ALIGNMENT);
        magazaButonu.addActionListener(e -> magazayiAcKapat());

        ozellikButonu=new JButton("Özellikleri aç");
        ozellikButonu.setFont(new Font("Arial",Font.BOLD,14));
        ozellikButonu.setAlignmentX(Component.CENTER_ALIGNMENT);
        ozellikButonu.addActionListener(e->ozelliklerAcKapat());

        anaPanel.add(kurabiyeLabel);
        anaPanel.add(Box.createVerticalStrut(10));
        anaPanel.add(kazancLabel);
        anaPanel.add(Box.createVerticalStrut(5));
        anaPanel.add(talimatLabel);
        anaPanel.add(Box.createVerticalStrut(15));
        anaPanel.add(kurabiyeButonu);
        anaPanel.add(Box.createVerticalStrut(15));
        anaPanel.add(magazaButonu);
        anaPanel.add(ozellikButonu);
        add(anaPanel, BorderLayout.CENTER);
    }
    private void ozellikPaneliolustur(){

        ozelliklerPaneli = new JPanel();
        ozelliklerPaneli.setLayout(new BoxLayout(ozelliklerPaneli, BoxLayout.Y_AXIS));
        ozelliklerPaneli.setBorder(BorderFactory.createTitledBorder("Özellik Mağazası"));
        ozelliklerPaneli.setVisible(false);

        JLabel ozellikBaslik = new JLabel("Özellikler", SwingConstants.CENTER);
        ozellikBaslik.setFont(new Font("Arial", Font.BOLD, 18));
        ozellikBaslik.setAlignmentX(Component.CENTER_ALIGNMENT);
        ozelliklerPaneli.add(ozellikBaslik);
        ozelliklerPaneli.add(Box.createVerticalStrut(10));
        for (Yukseltme ozellik : ozellikler) {

            JPanel ozellikPaneli = new JPanel();
            ozellikPaneli.setLayout(new BorderLayout());
            ozellikPaneli.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            ozellikPaneli.setMaximumSize(new Dimension(400, 60));

            JButton satinAlButonu2 = new JButton(ozellik.isim + " - " + ozellik.fiyat + " 🍪");
            satinAlButonu2.addActionListener(e -> ozellikSatinAl(ozellik));

            String ozelAciklama = "";
            if (ozellik.isim.equals("10x Kazanç (5 Tık)")) {
                ozelAciklama = "5 tık 10x kazanç";
            } else if (ozellik.isim.equals("2x Kazanç (10sn)")) {
                ozelAciklama = "10sn 2x kazanç";
            } else if (ozellik.isim.equals("Otomatik Artış")) {
                ozelAciklama = "Otomatik kazancı artırır";
            }
            JLabel etkiLabel = new JLabel(ozelAciklama);

            ozellikPaneli.add(satinAlButonu2,BorderLayout.CENTER);
            ozellikPaneli.add(etkiLabel,BorderLayout.WEST);

            ozelliklerPaneli.add(ozellikPaneli);
            ozelliklerPaneli.add(Box.createVerticalStrut(5));

        }
        JButton kapatbutonu2 = new JButton("Özellikler mağazasını kapat");
        kapatbutonu2.setAlignmentX(Component.CENTER_ALIGNMENT);
        kapatbutonu2.addActionListener(e->ozelliklerAcKapat());
        ozelliklerPaneli.add(Box.createVerticalStrut(10));
        ozelliklerPaneli.add(kapatbutonu2);

        add(ozelliklerPaneli,BorderLayout.WEST);


    }

    private void magazaPaneliOlustur() {
        magazaPaneli = new JPanel();
        magazaPaneli.setLayout(new BoxLayout(magazaPaneli, BoxLayout.Y_AXIS));
        magazaPaneli.setBorder(BorderFactory.createTitledBorder("🛒 Kurabiye Mağazası"));
        magazaPaneli.setVisible(false);

        JLabel magazaBaslik = new JLabel("Yükseltmeler", SwingConstants.CENTER);
        magazaBaslik.setFont(new Font("Arial", Font.BOLD, 18));
        magazaBaslik.setAlignmentX(Component.CENTER_ALIGNMENT);
        magazaPaneli.add(magazaBaslik);
        magazaPaneli.add(Box.createVerticalStrut(10));

        for (Yukseltme yukseltme : yukseltmeler) {
            JPanel yukseltmePaneli = new JPanel();
            yukseltmePaneli.setLayout(new BorderLayout());
            yukseltmePaneli.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            yukseltmePaneli.setMaximumSize(new Dimension(400, 60));

            JButton satinAlButonu = new JButton(yukseltme.isim + " - " + yukseltme.fiyat + " 🍪");
            satinAlButonu.addActionListener(e -> yukseltmeSatinAl(yukseltme));

            JLabel etkiLabel = new JLabel("+ " + yukseltme.tikGucu + " 🖱️ / + " + yukseltme.otomatikGuc + " ⏱️");

            yukseltmePaneli.add(satinAlButonu, BorderLayout.CENTER);
            yukseltmePaneli.add(etkiLabel, BorderLayout.EAST);

            magazaPaneli.add(yukseltmePaneli);
            magazaPaneli.add(Box.createVerticalStrut(5));
        }

        JButton kapatButonu = new JButton("Mağazayı Kapat");
        kapatButonu.setAlignmentX(Component.CENTER_ALIGNMENT);
        kapatButonu.addActionListener(e -> magazayiAcKapat());
        magazaPaneli.add(Box.createVerticalStrut(10));
        magazaPaneli.add(kapatButonu);

        add(magazaPaneli, BorderLayout.EAST);
    }

    private void kurabiyeTikla() {
        int kazanc = tikBasinaKazanc;

        if (aktif10xKazanc) {
            kurabiyeler += kazanc * 10;
            kalan10xTiklama--;

            if (kalan10xTiklama <= 0) {
                aktif10xKazanc = false;
            }
        } else {
            kurabiyeler += kazanc;
        }

        ekraniGuncelle();
    }

    private void yukseltmeSatinAl(Yukseltme yukseltme) {
        if (kurabiyeler >= yukseltme.fiyat) {
            kurabiyeler -= yukseltme.fiyat;
            tikBasinaKazanc += yukseltme.tikGucu;
            saniyeBasinaKazanc += yukseltme.otomatikGuc;
            ekraniGuncelle();
            JOptionPane.showMessageDialog(this, yukseltme.isim + " satın alındı!")
            ;
        } else {
            JOptionPane.showMessageDialog(this, "Yeterli kurabiyeniz yok!", "Uyarı", JOptionPane.WARNING_MESSAGE);
        }
    }
    private void ozellikSatinAl(Yukseltme ozellik) {
        if (kurabiyeler >= ozellik.fiyat) {
            kurabiyeler -= ozellik.fiyat;

            if (ozellik.isim.equals("10x Kazanç (5 Tık)")) {
                aktif10xKazanc = true;
                kalan10xTiklama = 5;
                talimatLabel.setText("10x KAZANÇ AKTİF! Kalan: " + kalan10xTiklama + " tık");
                talimatLabel.setForeground(Color.RED);
                JOptionPane.showMessageDialog(this, "Sonraki 5 tıklama için 10x kazanç aktif!");
            }
            else if (ozellik.isim.equals("2x Kazanç (10sn)")) {
                 int normalTikKazanc=tikBasinaKazanc; //isteğe bağlı final int olabilir final kullanırsak açıkça değişmeyecek demiş oluruz

                tikBasinaKazanc*=2;
                Timer timer = new Timer(10000, e -> {
                    talimatLabel.setText("Kurabiye kazanmak için resme tıklayın");
                    talimatLabel.setForeground(Color.BLACK);
                    tikBasinaKazanc=normalTikKazanc;

                });
                timer.setRepeats(false);
                timer.start();
                talimatLabel.setText("2x KAZANÇ AKTİF! 10 saniye");
                talimatLabel.setForeground(Color.BLUE);
                JOptionPane.showMessageDialog(this, "10 saniye boyunca 2x kazanç aktif!");
            }
            else if (ozellik.isim.equals("Otomatik Artış")) {
                saniyeBasinaKazanc += (int)(saniyeBasinaKazanc * 0.5);
                JOptionPane.showMessageDialog(this, "Otomatik kazancınız %50 arttı!");
            }

            ekraniGuncelle();
        } else {
            JOptionPane.showMessageDialog(this, "Yeterli kurabiyeniz yok!", "Uyarı", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void ekraniGuncelle() {
        kurabiyeLabel.setText("Kurabiyeler: " + kurabiyeler);
        kazancLabel.setText("Saniyede Kazanç: " + saniyeBasinaKazanc);
    }

    private void magazayiAcKapat() {
        boolean magazaAcik = magazaPaneli.isVisible();
        magazaPaneli.setVisible(!magazaAcik);
        magazaButonu.setVisible(magazaAcik);
        pack();
    }
    private void ozelliklerAcKapat()
    {
        boolean ozelliklerAcik=ozelliklerPaneli.isVisible();
        ozelliklerPaneli.setVisible(!ozelliklerAcik);
        ozellikButonu.setVisible(ozelliklerAcik);
        pack();

    }

    private void otomatikTiklayiciBaslat() {
        otomatikTiklayici = new Timer(1000, e -> {
            kurabiyeler += saniyeBasinaKazanc;
            ekraniGuncelle();
        });
        otomatikTiklayici.start();
    }

    private ImageIcon varsayilanKurabiyeResmi() {
        BufferedImage img = new BufferedImage(150, 150, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();

        g.setColor(new Color(210, 180, 140));
        g.fillOval(25, 25, 100, 100);

        g.setColor(new Color(139, 69, 19));
        g.setStroke(new BasicStroke(4));
        g.drawOval(25, 25, 100, 100);

        g.fillOval(45, 45, 20, 20);
        g.fillOval(85, 65, 20, 20);
        g.fillOval(65, 95, 20, 20);

        g.dispose();
        return new ImageIcon(img);
    }

    class Yukseltme {
        String isim;
        int fiyat;
        int otomatikGuc;
        int tikGucu;

        public Yukseltme(String isim, int fiyat, int otomatikGuc, int tikGucu) {
            this.isim = isim;
            this.fiyat = fiyat;
            this.otomatikGuc = otomatikGuc;
            this.tikGucu = tikGucu;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CookieClicker());
    }
}
