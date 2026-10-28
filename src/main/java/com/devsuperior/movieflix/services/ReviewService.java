package com.devsuperior.movieflix.services;

import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.projections.ReviewProjection;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.repositories.ReviewRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository repository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private AuthService authService;

    public List<ReviewDTO> findByMovie(Long movieId) {
       if(!movieRepository.existsById(movieId)) {
           throw new RuntimeException("Movie not found with id: " + movieId);
       }
       List<ReviewProjection> list = repository.searchByMovie(movieId);
       return list.stream().map(ReviewDTO::new).toList();
    }

    public ReviewDTO insert(@Valid ReviewDTO dto) {
        User user = authService.authenticated();

        try {
            Review entity = new Review();
            entity.setMovie(movieRepository.getReferenceById(dto.getMovie().getId()));
            entity.setUser(user);
            entity.setText(dto.getText());
            entity = repository.save(entity);

            return new ReviewDTO(entity);

        }
        catch (Exception e) {
            throw new RuntimeException("Error inserting review: " + e.getMessage());
        }
    }

}
