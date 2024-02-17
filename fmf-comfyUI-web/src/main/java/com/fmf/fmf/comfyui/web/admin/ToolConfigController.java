package com.fmf.fmf.comfyui.web.admin;

import com.fmf.fmf.comfyui.common.PR;
import com.fmf.fmf.comfyui.common.R;
import com.fmf.fmf.comfyui.dto.tool.ToolConfigInDTO;
import com.fmf.fmf.comfyui.dto.tool.ToolConfigOutDTO;
import com.fmf.fmf.comfyui.dto.tool.ToolConfigQueryInDTO;
import com.fmf.fmf.comfyui.service.ToolConfigService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author:吴金才
 * @Date:2024/2/17 10:35
 */
@RestController
@RequestMapping("/admin/toolConfig")
public class ToolConfigController {
    @Resource
    private ToolConfigService toolConfigService;

    @RequestMapping("/add")
    //用户填入参数，寻找模版
    public R addToolConfig(@RequestBody ToolConfigInDTO inDTO) {
        boolean addResult = toolConfigService.addToolConfig(inDTO);
        if (addResult) {
            return R.ok();
        } else {
            return R.fail();
        }
    }

    @RequestMapping("/update")
    //用户填入参数，寻找模版
    public R updateToolConfig(@RequestBody ToolConfigInDTO inDTO) {
        boolean updateResult = toolConfigService.updateToolConfig(inDTO);
        if (updateResult) {
            return R.ok();
        } else {
            return R.fail();
        }
    }

    @RequestMapping("/findAll")
    //用户填入参数，寻找模版
    public PR findAll(@RequestBody ToolConfigQueryInDTO inDTO) {
        PR resp = toolConfigService.findAll(inDTO);
        return resp;
    }

    @RequestMapping("/findDetail")
    //用户填入参数，寻找模版
    public R findDetail(@RequestBody ToolConfigInDTO inDTO) {
        ToolConfigOutDTO detail = toolConfigService.findDetail(inDTO);
        return R.ok(detail);
    }


    @RequestMapping("/delete")
    //用户填入参数，寻找模版
    public R deleteToolConfig(@RequestBody ToolConfigInDTO inDTO) {
        boolean deleteResult = toolConfigService.deleteToolConfig(inDTO);
        if (deleteResult) {
            return R.ok();
        } else {
            return R.fail();
        }
    }
}
