package com.lycmoons.entity.vo.request;
import lombok.Data;
import java.util.List;

@Data
public class DeleteNewsVO {
    List<Integer> newsIds;
}
