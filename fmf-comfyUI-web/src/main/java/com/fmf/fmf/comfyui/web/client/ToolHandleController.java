package com.fmf.fmf.comfyui.web.client;

import com.fmf.fmf.comfyui.common.R;
import com.fmf.fmf.comfyui.dto.ToolRequestDTO;
import com.fmf.fmf.comfyui.enums.ToolEnum;
import com.fmf.fmf.comfyui.strategy.ToolHandlerUtil;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @Author:吴金才
 * @Date:2024/2/1 11:01
 */
@RestController
@RequestMapping("/client")
public class ToolHandleController {
    @Resource
    private ToolHandlerUtil toolHandlerUtil;

    @RequestMapping("/doHandle")
    //用户填入参数，寻找模版
    public R doHandle(@RequestBody ToolRequestDTO toolRequest) {
        ToolEnum toolEnum = ToolEnum.getByCode(toolRequest.getToolCode());
        if (Objects.isNull(toolEnum)) {
            return R.fail("未匹配到有效工具");
        }
        toolHandlerUtil.doHandle(toolEnum, toolRequest);
        return R.ok();
    }
}
