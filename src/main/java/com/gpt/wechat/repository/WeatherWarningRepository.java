package com.gpt.wechat.repository;

import com.gpt.wechat.entity.UserEntity;
import com.gpt.wechat.entity.WeatherWarningEntity;
import com.zx.utils.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author KuiChi
 * @date 2023/4/29 10:45
 */
public interface WeatherWarningRepository extends BaseRepository<WeatherWarningEntity, Long> {

}
