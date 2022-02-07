package com.adamsoft.moviereview.service;

import com.adamsoft.moviereview.dto.MovieDTO;
import com.adamsoft.moviereview.dto.MovieImageDTO;
import com.adamsoft.moviereview.dto.PageRequestDTO;
import com.adamsoft.moviereview.dto.PageResultDTO;
import com.adamsoft.moviereview.entity.Movie;
import com.adamsoft.moviereview.entity.MovieImage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface MovieService {
    Long register(MovieDTO movieDTO);
    PageResultDTO<MovieDTO, Object[]> getList(PageRequestDTO requestDTO);
    MovieDTO getMovie(Long mno);

    default Map<String, Object> dtoToEntity(MovieDTO movieDTO){
        Map<String, Object> entityMap = new HashMap<>();

        Movie movie = Movie.builder()
                .mno(movieDTO.getMno())
                .title(movieDTO.getTitle())
                .build();
        entityMap.put("movie", movie);

        List<MovieImageDTO> imageDTOList = movieDTO.getImageDTOList();

        //MovielmageDT0 처리
        if(imageDTOList != null && imageDTOList.size() > 0 ) {
            List<MovieImage> movielmageList = imageDTOList.stream(). map(movieImageDTO ->{
                MovieImage movieimage = MovieImage.builder()
                        .path(movieImageDTO.getPath())
                        .imgName(movieImageDTO.getImgName())
                        .uuid(movieImageDTO.getUuid())
                        .movie(movie)
                        .build();
                return movieimage;
            }).collect(Collectors.toList());
            entityMap.put("imgList", movielmageList);
        }
        return entityMap;
    }

    default MovieDTO entitiesToDTO(Movie movie, List<MovieImage> movieImages, double avg, Long reviewCnt){
        MovieDTO movieDTO = MovieDTO.builder()
                .mno(movie.getMno())
                .title(movie.getTitle())
                .regDate(movie.getRegDate())
                .modDate(movie.getModDate())
                .build();

        List<MovieImageDTO> movielmageDTOList = movieImages.stream().map(movieImage -> {
            return MovieImageDTO.builder()
                    .imgName(movieImage.getImgName())
                    .path(movieImage.getPath())
                    .uuid(movieImage.getUuid ())
                    .build();
        }).collect(Collectors.toList());
        movieDTO.setImageDTOList(movielmageDTOList);

        movieDTO.setAvg(avg);
        movieDTO.setReviewCnt(reviewCnt);

        return movieDTO;
    }
}

