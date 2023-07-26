/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chj.easy.log.admin.message;

import com.chj.easy.log.core.model.LogAlarmContent;
import com.chj.easy.log.core.model.LogAlarmPlatform;
import com.chj.easy.log.core.service.CacheService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/25 16:06
 */
public abstract class AbstractMessageCenterService  {

    @Resource
    CacheService cacheService;

    protected abstract String alarmPlatformType();

    protected abstract String buildMessageContent(LogAlarmContent logAlarmContent);

    protected abstract void execute(String accessToken, String secret, String text, List<String> receiverList);

    public void sendAlarmMessage(LogAlarmContent logAlarmContent) {
        String text = buildMessageContent(logAlarmContent);
        String alarmPlatformId = logAlarmContent.getAlarmPlatformId();
        String alarmPlatformType = logAlarmContent.getAlarmPlatformType();
        LogAlarmPlatform logAlarmPlatform = cacheService.getAlarmPlatform(alarmPlatformType, alarmPlatformId);
        if (Objects.isNull(logAlarmPlatform)) {
            return;
        }
        execute(logAlarmPlatform.getAccessToken(), logAlarmPlatform.getSecret(), text, logAlarmContent.getReceiverList());
    }

}
