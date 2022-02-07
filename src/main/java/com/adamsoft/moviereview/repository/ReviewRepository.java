package com.adamsoft.moviereview.repository;

import com.adamsoft.moviereview.entity.Member;
import com.adamsoft.moviereview.entity.Movie;
import com.adamsoft.moviereview.entity.Review;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @EntityGraph(attributePaths = {"member"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Review> findByMovie(Movie movie);

    //member 에 해당하는 데이터를 삭제하는 메서드
    //void deleteByMember(Member member);

    //member 에 해당하는 데이터를 삭제하는 메서드
    @Modifying
    @Query("delete from Review mr where mr.member = :member")
    void deleteByMember(Member member);


}
