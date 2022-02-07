package com.adamsoft.moviereview.dto;

import com.adamsoft.moviereview.entity.Movie;
import com.adamsoft.moviereview.entity.MovieImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {
    private Long mno;
    private String title;

    //영화의 평균 평점
    private double avg;
    //리뷰 수 jpa의 count()
    private Long reviewCnt;
    private LocalDateTime regDate;
    private LocalDateTime modDate;


    @Builder.Default
    private List<MovieImageDTO> imageDTOList = new ArrayList<>();


}

