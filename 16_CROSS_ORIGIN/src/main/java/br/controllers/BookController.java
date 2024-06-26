package br.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.data.vo.v1.BookVO;
import br.services.BookServices;
import br.util.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/book/v1")
@Tag(name = "Book", description = "Endpoint for Managing People")
public class BookController {
    @Autowired
    BookServices service;

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
	@Operation(summary = "Find a book", description = "Find a book", tags = {"Book"},
			responses = {
				@ApiResponse(description = "Success", responseCode = "200", 	
					content = @Content(schema = @Schema(implementation = BookVO.class))
				),
				@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
				@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
				@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
				@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
				@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
			})
	public BookVO findById(@PathVariable(value = "id") Long id) throws Exception{
		return service.findById(id);
	}

    @GetMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
	@Operation(summary = "Finds ALL Books", description = "Finds ALL Books", tags = {"Book"},
				responses = {
					@ApiResponse(description = "Success", responseCode = "200", 	
						content = {
							@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = BookVO.class))
							)
						}),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
				})
	public List<BookVO> findAll(){
		return service.findAll();
	}

	@PostMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML}, 
				consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
	@Operation(summary = "ADD a New Book", description = "Find a Book", tags = {"Book"},
			responses = {
				@ApiResponse(description = "Success", responseCode = "200", 	
					content = @Content(schema = @Schema(implementation = BookVO.class))
				),
				@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
				@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
				@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
			})
	public BookVO create(@RequestBody BookVO book) throws Exception{
		return service.create(book);
	}

	@PutMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML}, 
				consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
	@Operation(summary = "Updates a Book", description = "Find a Book", tags = {"Book"},
			responses = {
				@ApiResponse(description = "Updated", responseCode = "200", 	
					content = @Content(schema = @Schema(implementation = BookVO.class))
				),
				@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
				@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
				@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
				@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
			})
	public BookVO update(@RequestBody BookVO Book) throws Exception{
		return service.update(Book);
	}

	@DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
	@Operation(summary = "Deletes a Book", description = "Find a Book", tags = {"Book"},
			responses = {
				@ApiResponse(description = "No Content", responseCode = "204", 	content = @Content),
				@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
				@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
				@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
				@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
			})
	public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) throws Exception{
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
