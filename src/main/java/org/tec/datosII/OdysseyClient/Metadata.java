package org.tec.datosII.OdysseyClient;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import com.wrapper.spotify.requests.data.search.simplified.SearchTracksRequest;
import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Metadata extends RecursiveTreeObject<Metadata> {
    private static final String clientId = "a06fede390b14f838c6b4310efb69c5b";
    private static final String clientSecret = "cd97e11e2fec427aa90095d9d7ace133";

    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .build();
    private static final ClientCredentialsRequest credentialsRequest = spotifyApi.clientCredentials().build();

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
    public Image cover;


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
                cover = new Image(new ByteArrayInputStream(tag.getAlbumImage()));

            }else{
                System.out.println("Other tag");
            }


        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public void update(){
        try{
           final ClientCredentials credentials = credentialsRequest.execute();

           spotifyApi.setAccessToken(credentials.getAccessToken());

            SearchTracksRequest searchRequest = spotifyApi.searchTracks(name).build();

            Track[] tracks = searchRequest.execute().getItems();

            for(Track track : tracks){
                System.out.println("Nombre: " + track.getName());
            }



        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
