package com.chj.easy.log.common.window;

import cn.hutool.core.date.SystemClock;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * description 滑动窗口。该窗口同样的key，都是单线程计算。
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/18 22:05
 */
@Getter
public class SlidingWindow {

    /**
     * 循环队列，就是装多个窗口用，该数量是windowSize的2倍
     */
    private AtomicInteger[] timeSlices;

    /**
     * 队列的总长度
     */
    private final int timeSliceSize;

    /**
     * 每个时间片的时长，以毫秒为单位
     */
    private final int timeMillisPerSlice;

    /**
     * 共有多少个时间片（即窗口长度）
     */
    private final int windowSize;

    /**
     * 该滑窗的起始创建时间，也就是第一个数据
     */
    private long beginTimestamp;

    /**
     * 最后一个数据的时间戳
     */
    private long lastAddTimestamp;

    public SlidingWindow(int timeMillisPerSlice, int windowSize) {
        this.timeMillisPerSlice = timeMillisPerSlice;
        this.windowSize = windowSize;
        // 保证存储在至少两个window
        this.timeSliceSize = windowSize * 2;
        reset();
    }

    /**
     * 初始化
     */
    private void reset() {
        beginTimestamp = SystemClock.now();
        //窗口个数
        AtomicInteger[] localTimeSlices = new AtomicInteger[timeSliceSize];
        for (int i = 0; i < timeSliceSize; i++) {
            localTimeSlices[i] = new AtomicInteger(0);
        }
        timeSlices = localTimeSlices;
    }

    /**
     * 计算当前所在的时间片的位置
     */
    private int locationIndex() {
        long now = SystemClock.now();
        //如果当前的key已经超出一整个时间片了，那么就直接初始化就行了，不用去计算了
        if (now - lastAddTimestamp > (long) timeMillisPerSlice * windowSize) {
            reset();
        }
        return (int) (((now - beginTimestamp) / timeMillisPerSlice) % timeSliceSize);
    }

    /**
     * 增加count个数量
     */
    public int addCount(int count) {
        //当前自己所在的位置，是哪个小时间窗
        int index = locationIndex();
        //然后清空自己前面windowSize到2*windowSize之间的数据格的数据
        //譬如1秒分4个窗口，那么数组共计8个窗口
        //当前index为5时，就清空6、7、8、1。然后把2、3、4、5的加起来就是该窗口内的总和
        clearFromIndex(index);
        int sum = 0;
        // 在当前时间片里继续+1
        sum += timeSlices[index].addAndGet(count);
        //加上前面几个时间片
        for (int i = 1; i < windowSize; i++) {
            sum += timeSlices[(index - i + timeSliceSize) % timeSliceSize].get();
        }

        lastAddTimestamp = SystemClock.now();

        return sum;
    }

    public int getWindowSum() {
        //当前自己所在的位置，是哪个小时间窗
        int index = locationIndex();
        //然后清空自己前面windowSize到2*windowSize之间的数据格的数据
        //譬如1秒分4个窗口，那么数组共计8个窗口
        //当前index为5时，就清空6、7、8、1。然后把2、3、4、5的加起来就是该窗口内的总和
        clearFromIndex(index);
        int sum = 0;
        // 在当前时间片里继续+1
        sum += timeSlices[index].get();
        //加上前面几个时间片
        for (int i = 1; i < windowSize; i++) {
            sum += timeSlices[(index - i + timeSliceSize) % timeSliceSize].get();
        }
        lastAddTimestamp = SystemClock.now();
        return sum;
    }

    private void clearFromIndex(int index) {
        for (int i = 1; i <= windowSize; i++) {
            int j = index + i;
            if (j >= windowSize * 2) {
                j -= windowSize * 2;
            }
            timeSlices[j].set(0);
        }
    }
}