package org.tec.datosII.OdysseyClient;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Metadata extends RecursiveTreeObject<Metadata> {
    private static final String[] genres =
            {"Blues",
            "Classic Rock",
            "Country",
            "Dance",
            "Disco",
            "Funk",
            "Grunge",
            "Hip-Hop",
            "Jazz",
            "Metal",
            "New Age",
            "Oldies",
            "Other",
            "Pop",
            "R&B",
            "Rap",
            "Reggae",
            "Rock",
            "Techno",
            "Industrial",
            "Alternative",
            "Ska",
            "Death Metal",
            "Pranks",
            "Soundtrack",
            "Euro-Techno",
            "Ambient",
            "Trip-Hop",
            "Vocal",
            "Jazz+Funk",
            "Fusion",
            "Trance",
            "Classical",
            "Instrumental",
            "Acid",
            "House",
            "Game",
            "Sound Clip",
            "Gospel",
            "Noise",
            "Alt. Rock",
            "Bass",
            "Soul",
            "Punk",
            "Space",
            "Meditative",
            "Instrumental Pop",
            "Instrumental Rock",
            "Ethnic",
            "Gothic",
            "Darkwave",
            "Techno-Industrial",
            "Electronic",
            "Pop-Folk",
            "Eurodance",
            "Dream",
            "Southern Rock",
            "Comedy",
            "Cult",
            "Gangsta Rap",
            "Top 40",
            "Christian Rap",
            "Pop/Funk",
            "Jungle",
            "Native American",
            "Cabaret",
            "New Wave",
            "Psychedelic",
            "Rave",
            "Showtunes",
            "Trailer",
            "Lo-Fi",
            "Tribal",
            "Acid Punk",
            "Acid Jazz",
            "Polka",
            "Retro",
            "Musical",
            "Rock & Roll",
            "Hard Rock"};

    public String name = "";
    public String artist = "";
    public String year = "";
    public String album = "";
    public String genre = "";
    public String lyrics = "";
    public BufferedImage cover;


    public Metadata(String path){
        try {
            Mp3File mp3File = new Mp3File(path);
            if(mp3File.hasId3v1Tag()){
                System.out.println("Id3V1");

                ID3v1 tag = mp3File.getId3v1Tag();
                name = tag.getTitle();
                artist = tag.getArtist();
                year = tag.getYear();
                album = tag.getAlbum();
                genre = genres[tag.getGenre()];

            }else if(mp3File.hasId3v2Tag()){
                System.out.println("Id3V2");
                ID3v2 tag = mp3File.getId3v2Tag();
                name = tag.getTitle();
                artist = tag.getArtist();
                year = tag.getYear();
                album = tag.getAlbum();
                genre = genres[tag.getGenre()];
                cover = ImageIO.read(new ByteArrayInputStream(tag.getAlbumImage()));


            }else{
                System.out.println("Other tag");
            }


        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public Metadata(){}

    public void addLyrics(){
        String BASE_URL = "http://api.chartlyrics.com/apiv1.asmx/SearchLyricDirect";

        try {
            String parameters = "?artist=" + URLEncoder.encode(artist, "UTF-8") + "&song=" + URLEncoder.encode(name, "UTF-8");
            URL url = new URL(BASE_URL + parameters);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));

            StringBuffer content = new StringBuffer();

            int size;
            char[] buf = new char[4096];
            while ((size = in.read(buf)) != -1) {
                content.append(new String(buf, 0, size));
            }
            in.close();

            Document document = DocumentHelper.parseText(content.toString());
            Element root = document.getRootElement();
            String lyrics = root.elementIterator("Lyric").next().getText();
            this.lyrics = lyrics;

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
