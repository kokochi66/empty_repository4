package com.memorial.st.mst.controller.content;

import com.memorial.st.mst.domain.content.MstContent;
import com.memorial.st.mst.domain.content.enumType.MusicType;
import com.memorial.st.mst.domain.content.model.MstMusic;
import com.memorial.st.mst.service.content.MusicService;
import com.memorial.st.mst.service.content.repository.ContentRepository;
import com.memorial.st.mst.service.file.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/content")
public class ContentController {

    // 파일 클리어

    @Autowired
    private FileService fileService;
    @Autowired
    private ContentRepository contentRepository;

    @GetMapping("/file/clear")
    public Boolean clearContentFile() {
        List<MstContent> contentList = contentRepository.findAll();
        for (MstContent mstContent : contentList) {
            if (mstContent.getFileSrc() != null) {
                log.info("TEST :: " + mstContent.getFileSrc());
                fileService.deleteByFileName(mstContent.getFileSrc());
            }
        }
        return true;
    }
}
