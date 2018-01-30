package org.superbiz.moviefun.moviesapi;

import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

@Controller
public class HomeController {

    private final MoviesClient moviesClient;
    private final AlbumsClient albumsClient;
    private final MovieFixtures movieFixtures;
    private final AlbumFixtures albumFixtures;

    public HomeController(MoviesClient moviesClient, AlbumsClient albumsClient, MovieFixtures movieFixtures, AlbumFixtures albumFixtures) {
        this.moviesClient = moviesClient;
        this.albumsClient = albumsClient;
        this.movieFixtures = movieFixtures;
        this.albumFixtures = albumFixtures;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/setup")
    public String setup(Map<String, Object> model) {
        for (MovieInfo movie : movieFixtures.load()) {
            moviesClient.addMovie(movie);
        }

        for (AlbumInfo album : albumFixtures.load()) {
            albumsClient.addAlbum(album);
        }

        model.put("movies", moviesClient.getMovies());
        model.put("albums", albumsClient.getAlbums());

        return "setup";
    }

//    @GetMapping("/moviefun")
//    public String moviefun(Map<String, Object> model) {
//
//        model.put("movies", moviesClient.getMovies());
//        return "moviefun";
//    }
//
//    @PostMapping("/moviefun")
//    public String addMovie(MovieInfo movieInfo, Map<String, Object> model) {
//
//        moviesClient.addMovie(movieInfo);
//        model.put("movies", moviesClient.getMovies());
//        return "moviefun";
//    }

    @GetMapping("/albums")
    public String albums(Map<String, Object> model) {

        model.put("albums", albumsClient.getAlbums());
        return "albums";
    }

    @GetMapping("/albums/{id}")
    public String albumDetails(@PathVariable long id, Map<String, Object> model) {

        model.put("album", albumsClient.find(id));
        return "albumDetails";
    }


    @PostMapping("/albums/{albumId}/cover")
    public String uploadCover(@PathVariable Long albumId, @RequestParam("file") MultipartFile uploadedFile) {

        try {
            albumsClient.uploadCover(albumId, uploadedFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return format("redirect:/albums/%d", albumId);
    }

    @GetMapping("/albums/{albumId}/cover")
    public HttpEntity<byte[]> getCover(@PathVariable long albumId) {

        return new HttpEntity<>(albumsClient.getCover(albumId));
    }

//    @DeleteMapping("/{movieId}")
//    public String deleteMovieId(@PathVariable Long movieId, Map<String, Object> model) {
//        moviesClient.deleteMovieId(movieId);
//        model.put("movies", moviesClient.getMovies());
//        return "moviefun";
//    }

//    @GetMapping
//    public String find(
//            @RequestParam(name = "field", required = false) String field,
//            @RequestParam(name = "key", required = false) String key,
//            @RequestParam(name = "start", required = false) Integer start,
//            @RequestParam(name = "pageSize", required = false) Integer pageSize,
//            Map<String, Object> model
//    ) {
//        if (field != null && key != null) {
//            model.put("movies", moviesClient.findRange(field, key, start, pageSize));
//        } else if (start != null && pageSize != null) {
//            model.put("movies", moviesClient.findAll(start, pageSize));
//        } else {
//            model.put("movies", moviesClient.getMovies());
//        }
//
//        return "moviefun";
//    }

}
