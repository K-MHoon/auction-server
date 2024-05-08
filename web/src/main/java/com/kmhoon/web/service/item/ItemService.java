package com.kmhoon.web.service.item;

import com.kmhoon.common.model.entity.auth.user.User;
import com.kmhoon.common.model.entity.service.item.Item;
import com.kmhoon.common.repository.service.item.ItemRepository;
import com.kmhoon.web.controller.dto.item.request.ItemControllerRequest;
import com.kmhoon.web.exception.AuctionApiException;
import com.kmhoon.web.service.dto.item.ItemServiceRequestDto;
import com.kmhoon.web.service.user.UserCommonService;
import com.kmhoon.web.utils.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static com.kmhoon.web.exception.code.service.item.ItemErrorCode.HAS_NOT_SEQ_REQUEST;
import static com.kmhoon.web.exception.code.service.item.ItemErrorCode.OVER_ITEM_LIMIT;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {

    private final CustomFileUtil fileUtil;
    private final ItemRepository itemRepository;
    private final UserCommonService userCommonService;

    @Transactional
    public void add(ItemServiceRequestDto.Add request) {
        User loggedInUser = userCommonService.getLoggedInUser();
        int count = itemRepository.countByInventoryAndIsUseIsTrue(loggedInUser.getInventory());

        if(count > loggedInUser.getInventory().getItemLimit()) {
            throw new AuctionApiException(OVER_ITEM_LIMIT);
        }

        List<MultipartFile> images = request.getImages();
        List<String> uploadImageFileNames = fileUtil.saveFiles(images);

        List<MultipartFile> documents = request.getDocuments();
        List<String> uploadDocumentFileNames = fileUtil.saveFiles(documents);

        Item item = Item.builder()
                .type(request.getType())
                .description(request.getDescription())
                .name(request.getName())
                .isUse(Boolean.TRUE)
                .inventory(loggedInUser.getInventory())
                .build();

        uploadImageFileNames.forEach(item::addImage);
        uploadDocumentFileNames.forEach(item::addDocument);

        itemRepository.save(item);
    }

    public ResponseEntity<Resource> view(String fileName) {
        return fileUtil.getFile(fileName);
    }

    @Transactional
    public void delete(ItemControllerRequest.Delete request) {
        User loggedInUser = userCommonService.getLoggedInUser();
        List<Item> itemList = itemRepository.findAllByInventoryAndIsUseIsTrue(loggedInUser.getInventory());

        List<Item> deleteTargetItemList = itemList.stream().filter(item -> request.getSeqList().contains(item.getSequence())).collect(Collectors.toList());

        if(deleteTargetItemList.size() != request.getSeqList().size()) {
            throw new AuctionApiException(HAS_NOT_SEQ_REQUEST);
        }

        deleteTargetItemList.forEach(Item::delete);
    }
}
