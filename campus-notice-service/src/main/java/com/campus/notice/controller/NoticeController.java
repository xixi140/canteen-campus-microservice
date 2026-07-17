package com.campus.notice.controller;

import com.campus.common.result.R;
import com.campus.notice.entity.Notice;
import com.campus.notice.websocket.NoticeWebSocket;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 通知 Controller
 *
 * 提供创建通知、查询通知列表、标记已读的 REST API
 */
@RestController
@RequestMapping("/api/notice")
public class NoticeController {

    /** 模拟通知数据存储 */
    private static final Map<Long, Notice> NOTICE_MAP = new ConcurrentHashMap<>();
    private static final AtomicLong ID_GEN = new AtomicLong(1);

    /**
     * 创建通知
     *
     * 请求示例：
     * POST /api/notice/create
     * {
     *   "userId": 1,
     *   "title": "订单通知",
     *   "content": "您的订单已支付成功",
     *   "type": "ORDER_STATUS"
     * }
     */
    @PostMapping("/create")
    public R<Notice> createNotice(@RequestBody Notice notice) {
        long id = ID_GEN.getAndIncrement();
        notice.setId(id);
        notice.setIsRead(false);
        notice.setCreateTime(LocalDateTime.now());
        NOTICE_MAP.put(id, notice);
        // WebSocket 推送通知
        String msg = String.format("{\"type\":\"%s\",\"title\":\"%s\",\"content\":\"%s\"}", notice.getType(), notice.getTitle(), notice.getContent());
        NoticeWebSocket.sendToUser(notice.getUserId(), msg);
        return R.ok(notice);
    }

    /**
     * 查询用户的通知列表
     *
     * @param userId 用户ID
     */
    @GetMapping("/list")
    public R<List<Notice>> listNotices(@RequestParam Long userId) {
        List<Notice> list = NOTICE_MAP.values().stream()
                .filter(n -> n.getUserId().equals(userId))
                .sorted((a, b) -> b.getCreateTime().compareTo(a.getCreateTime()))
                .collect(Collectors.toList());
        return R.ok(list);
    }

    /**
     * 标记通知为已读
     */
    @PutMapping("/{id}/read")
    public R<Notice> markAsRead(@PathVariable Long id) {
        Notice notice = NOTICE_MAP.get(id);
        if (notice == null) {
            return R.fail("通知不存在");
        }
        notice.setIsRead(true);
        return R.ok(notice);
    }
}
