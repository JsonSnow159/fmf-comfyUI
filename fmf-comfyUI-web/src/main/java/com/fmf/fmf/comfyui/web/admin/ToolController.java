package com.fmf.fmf.comfyui.web.admin;

import com.fmf.fmf.comfyui.common.PR;
import com.fmf.fmf.comfyui.common.PageSearchDTO;
import com.fmf.fmf.comfyui.common.R;
import com.fmf.fmf.comfyui.dto.tool.ToolAddInDTO;
import com.fmf.fmf.comfyui.dto.tool.ToolQueryInDTO;
import com.fmf.fmf.comfyui.dto.tool.ToolQueryOutDTO;
import com.fmf.fmf.comfyui.service.ToolService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author:吴金才
 * @Date:2024/2/7 12:17
 */
@RestController
@RequestMapping("/admin/tool")
public class ToolController {
    @Resource
    private ToolService toolService;

    @RequestMapping("/add")
    //用户填入参数，寻找模版
    public R addTool(@RequestBody ToolAddInDTO inDTO) {
        boolean addResult = toolService.addTool(inDTO);
        if (addResult) {
            return R.ok();
        } else {
            return R.fail();
        }
    }

    @RequestMapping("/update")
    //用户填入参数，寻找模版
    public R updateTool(@RequestBody ToolAddInDTO inDTO) {
        boolean addResult = toolService.updateTool(inDTO);
        if (addResult) {
            return R.ok();
        } else {
            return R.fail();
        }
    }

    /**
     * 查询全部
     *
     * @return
     */
    @PostMapping
    @RequestMapping("/findAll")
    public PR findAllTool(@RequestBody PageSearchDTO pageSearchDTO) {
        PR resp = toolService.findAll(pageSearchDTO);
        return resp;
    }

    @RequestMapping("/findDetail")
    //用户填入参数，寻找模版
    public R findDetail(@RequestBody ToolQueryInDTO inDTO) {
        ToolQueryOutDTO detail = toolService.findDetail(inDTO);
        return R.ok(detail);
    }

    @RequestMapping("/delete")
    //用户填入参数，寻找模版
    public R delete(@RequestBody ToolQueryInDTO inDTO) {
        boolean deleteResult = toolService.deleteTool(inDTO);
        if (deleteResult) {
            return R.ok();
        } else {
            return R.fail();
        }
    }
}
