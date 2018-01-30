package org.superbiz.moviefun.moviesapi;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestOperations;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import org.superbiz.moviefun.blobstore.Blob;
import org.superbiz.moviefun.blobstore.BlobStore;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class AlbumsClient {


    private String albumsUrl;
    private RestOperations restOperations;
    private BlobStore blobStore;

    private static ParameterizedTypeReference<List<AlbumInfo>> albumsListType = new ParameterizedTypeReference<List<AlbumInfo>>() {
    };

    public AlbumsClient(String albumsUrl, RestOperations restOperations, BlobStore blobStore) {
        this.albumsUrl = albumsUrl;
        this.restOperations = restOperations;
        this.blobStore = blobStore;
    }


    public void addAlbum(AlbumInfo album) {
        restOperations.postForEntity(albumsUrl, album, AlbumInfo.class);
    }

    public AlbumInfo find(long id) {
        return restOperations.getForEntity(albumsUrl + "/" + String.valueOf(id), AlbumInfo.class).getBody();
    }

    public List<AlbumInfo> getAlbums() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(albumsUrl);

        return restOperations.exchange(builder.toUriString(), HttpMethod.GET, null, albumsListType).getBody();
    }

    public void deleteAlbum(AlbumInfo album) {
        restOperations.delete(albumsUrl + "/" + album.getId());
    }

    public void updateAlbum(AlbumInfo album) {
        restOperations.put(albumsUrl, album);
    }

    public void uploadCover(Long albumId, MultipartFile uploadedFile) throws IOException {

        //logger.fine("Calling album service to upload");

//        return restOperations.exchange(albumsUrl + "/" + String.valueOf(albumId) + "/cover",
//                HttpMethod.POST,
//                null,
//                String.class,
//                uploadedFile).getBody();

        Blob coverBlob = new Blob("covers/" + Long.toString(albumId),
                uploadedFile.getInputStream(),
                uploadedFile.getContentType());

        blobStore.put(coverBlob);

    }

    public byte[] getCover(long albumId) {

        //logger.fine("calling album service to get");

        return restOperations.getForObject(albumsUrl + "/" + String.valueOf(albumId) + "/cover",
                byte[].class);
    }
}
