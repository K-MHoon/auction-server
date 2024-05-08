package com.kmhoon.web.controller.item;

import com.kmhoon.web.controller.dto.item.request.ItemControllerRequest;
import com.kmhoon.web.service.item.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    @GetMapping("/api/view/item/{fileName}")
    public ResponseEntity<Resource> viewFileGet(@PathVariable(name = "fileName") String fileName) {
        return itemService.view(fileName);
    }

    @PostMapping("/api/service/item")
    @ResponseStatus(HttpStatus.OK)
    public void add(@Valid ItemControllerRequest.Add request) {
        log.info("add : " + request);
        itemService.add(request.toServiceRequest());
    }

    @DeleteMapping("/api/service/item")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestBody @Valid ItemControllerRequest.Delete request) {
        log.info("delete : " + request);
        itemService.delete(request);
    }
}
