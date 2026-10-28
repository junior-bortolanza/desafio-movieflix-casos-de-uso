package com.devsuperior.movieflix.services;


import com.devsuperior.movieflix.dto.MovieCardDTO;
import com.devsuperior.movieflix.dto.MovieDetailsDTO;
import com.devsuperior.movieflix.entities.Genre;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.repositories.GenreRepository;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    private MovieRepository repository;

    @Autowired
    private GenreRepository genreRepository;

    @Transactional(readOnly = true)
    public Page<MovieCardDTO> findByGenre(Long genreId, Pageable pageable) {
        Genre genre = (genreId == null || 0 == genreId) ? null : genreRepository.getReferenceById(genreId);
        Page<Movie> page = repository.searchByGenre(genre, pageable);
        return page.map(MovieCardDTO::new);
    }

    @Transactional(readOnly = true)
    public MovieDetailsDTO findById(Long id) {
        Optional<Movie> obj = repository.findById(id);
        Movie entity = obj.orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));
        return new MovieDetailsDTO(entity);
    }

}


