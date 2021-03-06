package com.review.everything.web;

import com.review.everything.service.reviews.ReviewsService;
import com.review.everything.web.dto.ReviewsResponseDto;
import com.review.everything.web.dto.ReviewsSaveRequestDto;
import com.review.everything.web.dto.ReviewsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RequiredArgsConstructor
@RestController
public class ReviewsApiController {


    @Value("${storage.location}")
    private String fileRealPath;

    private final ReviewsService service;

    @PostMapping("/api/v1/reviews")
    public Long save(@RequestBody ReviewsSaveRequestDto requestDto) {
        return service.save(requestDto);
    }

    @PutMapping("/api/v1/reviews/{id}")
    public Long update(@PathVariable Long id, @RequestBody ReviewsUpdateRequestDto requestDto) {
        String oldFileName = service.findById(id).getImg();
        if (!oldFileName.equals("") || !oldFileName.equals(null)) {
            if (!oldFileName.equals(requestDto.getImg())) {
                fileDelete(fileRealPath + oldFileName);
            }
        } else if (oldFileName.equals(requestDto.getImg())) {
            fileDelete(fileRealPath + oldFileName);
        }
        return service.update(id, requestDto);
    }

    @GetMapping("/api/v1/reviews/{id}")
    public ReviewsResponseDto findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @DeleteMapping("/api/v1/reviews/{id}")
    public Long delete(@PathVariable Long id) {
        String fileName = findById(id).getImg();
        fileDelete(fileRealPath + fileName);
        service.delete(id);
        return id;
    }

    public void fileDelete(String filePath) {
        File file = new File(filePath);
        if(file.exists() == true){
            file.delete();
        }
    }
}
