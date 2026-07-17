package com.campus.canteen.controller;

import com.campus.canteen.entity.Canteen;
import com.campus.canteen.entity.Stall;
import com.campus.common.result.R;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 食堂与档口 Controller
 *
 * 提供食堂列表、食堂详情、档口列表的 REST API
 */
@RestController
@RequestMapping("/api/canteen")
public class CanteenController {

    /** 模拟食堂数据 */
    private static final Map<Long, Canteen> CANTEEN_MAP = new ConcurrentHashMap<>();
    /** 模拟档口数据 */
    private static final Map<Long, List<Stall>> STALL_MAP = new ConcurrentHashMap<>();

    static {
        // ====== 食堂数据 ======
        Canteen c1 = new Canteen();
        c1.setId(1L);
        c1.setName("一食堂");
        c1.setLocation("校园南区");
        c1.setOpenTime("06:30-21:00");
        c1.setImageUrl("https://campus-canteen.oss/canteen/1.jpg");
        c1.setStallCount(12);

        Canteen c2 = new Canteen();
        c2.setId(2L);
        c2.setName("二食堂");
        c2.setLocation("校园北区");
        c2.setOpenTime("06:30-22:00");
        c2.setImageUrl("https://campus-canteen.oss/canteen/2.jpg");
        c2.setStallCount(10);

        Canteen c3 = new Canteen();
        c3.setId(3L);
        c3.setName("三食堂（教工食堂）");
        c3.setLocation("校园东区");
        c3.setOpenTime("07:00-20:00");
        c3.setImageUrl("https://campus-canteen.oss/canteen/3.jpg");
        c3.setStallCount(8);

        CANTEEN_MAP.put(1L, c1);
        CANTEEN_MAP.put(2L, c2);
        CANTEEN_MAP.put(3L, c3);

        // ====== 档口数据 ======
        STALL_MAP.put(1L, buildStalls(c1.getId()));
        STALL_MAP.put(2L, buildStalls(c2.getId()));
        STALL_MAP.put(3L, buildStalls(c3.getId()));
    }

    private static List<Stall> buildStalls(Long canteenId) {
        List<Stall> stalls = new ArrayList<>();
        String[] categories = {"炒菜", "面食", "小吃", "饮品"};
        for (int i = 0; i < categories.length; i++) {
            Stall s = new Stall();
            s.setId(canteenId * 100L + i);
            s.setCanteenId(canteenId);
            s.setName((i + 1) + "号窗口");
            s.setCategory(categories[i]);
            s.setSortOrder(i);
            stalls.add(s);
        }
        return stalls;
    }

    /**
     * 获取所有食堂列表
     */
    @GetMapping("/list")
    public R<List<Canteen>> listCanteens() {
        return R.ok(new ArrayList<>(CANTEEN_MAP.values()));
    }

    /**
     * 获取单个食堂详情
     */
    @GetMapping("/{id}")
    public R<Canteen> getCanteen(@PathVariable Long id) {
        Canteen canteen = CANTEEN_MAP.get(id);
        if (canteen == null) {
            return R.fail("食堂不存在");
        }
        return R.ok(canteen);
    }

    /**
     * 获取某食堂下的档口列表
     */
    @GetMapping("/{id}/stalls")
    public R<List<Stall>> listStalls(@PathVariable Long id) {
        if (!CANTEEN_MAP.containsKey(id)) {
            return R.fail("食堂不存在");
        }
        return R.ok(STALL_MAP.getOrDefault(id, List.of()));
    }
}
