package com.ziqiang.sushuodorm.services.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ziqiang.sushuodorm.entity.item.MsgItem;
import com.ziqiang.sushuodorm.mapper.MsgMapper;
import com.ziqiang.sushuodorm.services.impl.MsgServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MsgServiceImplTest {

    @Mock
    private MsgMapper msgMapper;

    @InjectMocks
    private MsgServiceImpl msgService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveItemReturnSuccess() {
        // Arrange
        when(msgMapper.insertOrUpdate(any(MsgItem.class))).thenReturn(true);

        // Act
        boolean result = msgService.saveItem("user1", "Hello World");

        // Assert
        assertTrue(result);
        verify(msgMapper, times(1)).insertOrUpdate(any(MsgItem.class));
    }

    @Test
    void testSaveItemReturnFailure() {
        // Arrange
        when(msgMapper.insertOrUpdate(any(MsgItem.class))).thenReturn(false);

        // Act
        boolean result = msgService.saveItem("user1", "Hello World");

        // Assert
        assertFalse(result);
        verify(msgMapper, times(1)).insertOrUpdate(any(MsgItem.class));
    }

    @Test
    void testGetMsgListReturnWithMessages() {
        // Arrange
        MsgItem msgItem = new MsgItem().setAuthor("user1").setContent("Hello World");
        when(msgMapper.selectList(any())).thenReturn(Arrays.asList(msgItem));

        // Act
        List<MsgItem> result = msgService.getMsgList("user1");

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(msgMapper, times(1)).selectList(any());
    }

    @Test
    void testGetMsgListReturnNoMessages() {
        // Arrange
        when(msgMapper.selectList(any())).thenReturn(Collections.emptyList());

        // Act
        List<MsgItem> result = msgService.getMsgList("user1");

        // Assert
        assertTrue(result.isEmpty());
        verify(msgMapper, times(1)).selectList(any());
    }

    @Test
    void testGetPageReturnWithMessages() {
        // Arrange
        MsgItem msgItem = new MsgItem().setAuthor("user1").setContent("Hello World");
        when(msgMapper.selectList(any())).thenReturn(Arrays.asList(msgItem));

        // Act
        Page<MsgItem> result = msgService.getPage("user1", 1, 10);

        // Assert
        assertFalse(result.getRecords().isEmpty());
        assertEquals(1, result.getRecords().size());
        verify(msgMapper, times(1)).selectList(any());
    }

    @Test
    void testGetPageReturnNoMessages() {
        // Arrange
        when(msgMapper.selectList(any())).thenReturn(Collections.emptyList());

        // Act
        Page<MsgItem> result = msgService.getPage("user1", 1, 10);

        // Assert
        assertTrue(result.getRecords().isEmpty());
        verify(msgMapper, times(1)).selectList(any());
    }
}
