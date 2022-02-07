package com.adamsoft.moviereview.service;

import com.adamsoft.moviereview.dto.ReviewDTO;
import com.adamsoft.moviereview.entity.Movie;
import com.adamsoft.moviereview.entity.Review;
import com.adamsoft.moviereview.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    public List<ReviewDTO> getList(Long mno) {
        log.info(mno);
        Movie movie = Movie.builder().mno(mno).build();
        List<Review> result = reviewRepository.findByMovie(movie);
        return result.stream().map(movieReview -> entityToDTO(movieReview)).collect(Collectors.toList());
    }

    @Override
    public Long register(ReviewDTO reviewDTO) {
        Review review = dtoToEntity(reviewDTO);
        reviewRepository.save(review);
        return review.getReviewnum();
    }


    @Override
    public void remove(Long rnum) {
        log.info("remove " + rnum);
        reviewRepository.deleteById(rnum);
    }

    @Override
    public void modify(ReviewDTO reviewDTO) {
        Optional<Review> result = reviewRepository.findById(reviewDTO.getReviewnum());
        if(result.isPresent()){
            Review movieReview = result.get(); movieReview.changeGrade(reviewDTO.getGrade()); movieReview.changeText(reviewDTO.getText());
            reviewRepository.save(movieReview);
        }
    }
}
