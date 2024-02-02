package com.fmf.fmf.comfyui.service.impl;

import com.alibaba.fastjson.JSON;
import com.fmf.fmf.comfyui.common.HttpCallRestTemplate;
import com.fmf.fmf.comfyui.dto.*;
import com.fmf.fmf.comfyui.enums.TimeoutLevelEnum;
import com.fmf.fmf.comfyui.exception.BizException;
import com.fmf.fmf.comfyui.exception.ErrorCodeEnum;
import com.fmf.fmf.comfyui.service.ComfyUIAPIService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author:吴金才
 * @Date:2024/1/30 16:21
 */
@Slf4j
@Service
public class ComfyUIAPIServiceImpl implements ComfyUIAPIService {
    @Resource
    private HttpCallRestTemplate httpCallRestTemplate;

    @Override
    public QueuePromptResponse prompt(String serviceAddress, String prompt) {
//        this.doQueue();
        HttpCallContext<String> httpCallContext = new HttpCallContext();
        String tripartiteUrl = "http://" + serviceAddress + "/prompt";
        httpCallContext.setUrl(tripartiteUrl);
        httpCallContext.setMethod(HttpMethod.POST.toString());
        httpCallContext.setBody(prompt);
        Map<String, String> headers = new HashMap<>();
        httpCallContext.setHeaders(headers);
        httpCallContext.setTimeoutLevel(TimeoutLevelEnum.LEVEL_MEDIUM);

        HttpCallResponse response = httpCallRestTemplate.doInvoke(httpCallContext);

        if (HttpStatus.SC_OK != response.getStatusCode()) {
            log.error("三方HTTP请求异常：{}", response.getErrorMessage());
            throw new BizException(ErrorCodeEnum.HTTP_CALL_5XX_ERROR.getCode(), ErrorCodeEnum.HTTP_CALL_5XX_ERROR.getMsg());
        }
        String httpData = response.getData();
        return JSON.parseObject(httpData, QueuePromptResponse.class);
    }

    public void doQueue() {
        try {
            // 创建 RestTemplate 实例
            RestTemplate restTemplate = new RestTemplate();
            String apiUrl = "http://127.0.0.1:8188/prompt";
            URI uri = URI.create(apiUrl);
            // 定义请求方法为 POST
            HttpMethod httpMethod = HttpMethod.POST;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String requestBody = "{\"client_id\":\"f75086e60ecb40f795d57c9baefbc203\",\"prompt\":{\"3\":{\"inputs\":{\"seed\":156680208700286,\"steps\":5,\"cfg\":5,\"sampler_name\":\"euler\",\"scheduler\":\"normal\",\"denoise\":1,\"model\":[\"10\",0],\"positive\":[\"6\",0],\"negative\":[\"7\",0],\"latent_image\":[\"5\",0]},\"class_type\":\"KSampler\"},\"4\":{\"inputs\":{\"ckpt_name\":\"anythingelseV4_v45.safetensors\"},\"class_type\":\"CheckpointLoaderSimple\"},\"5\":{\"inputs\":{\"width\":512,\"height\":512,\"batch_size\":1},\"class_type\":\"EmptyLatentImage\"},\"6\":{\"inputs\":{\"text\":\"masterpiece, best quality, 1girl, solo,long_hair,\\nlooking_at_viewer, smile, bangs, skirt, shirt, long_sleeves, hat,\\ndress, bow, holding, closed_mouth, flower, frills, hair_flower\\npetals, bouquet, holding_flower, center_frills, boonnet\\nholding bouquet, flower field, flower field, lineart,monochrome,\",\"clip\":[\"10\",1]},\"class_type\":\"CLIPTextEncode\"},\"7\":{\"inputs\":{\"text\":\"text, watermark\",\"clip\":[\"10\",1]},\"class_type\":\"CLIPTextEncode\"},\"8\":{\"inputs\":{\"samples\":[\"3\",0],\"vae\":[\"11\",0]},\"class_type\":\"VAEDecode\"},\"9\":{\"inputs\":{\"filename_prefix\":\"ComfyUI\",\"images\":[\"8\",0]},\"class_type\":\"SaveImage\"},\"10\":{\"inputs\":{\"lora_name\":\"LCM_LoRA_Weights_SD15.safetensors\",\"strength_model\":1,\"strength_clip\":1,\"model\":[\"4\",0],\"clip\":[\"4\",1]},\"class_type\":\"LoraLoader\"},\"11\":{\"inputs\":{\"vae_name\":\"orangemixs_abyss2Sfw.safetensors\"},\"class_type\":\"VAELoader\"}},\"extra_data\":{\"extra_pnginfo\":{\"workflow\":{\"last_node_id\":11,\"last_link_id\":17,\"nodes\":[{\"id\":5,\"type\":\"EmptyLatentImage\",\"pos\":[520,1280],\"size\":{\"0\":315,\"1\":106},\"flags\":{},\"order\":0,\"mode\":0,\"outputs\":[{\"name\":\"LATENT\",\"type\":\"LATENT\",\"links\":[2],\"slot_index\":0}],\"properties\":{\"Node name for S&R\":\"EmptyLatentImage\"},\"widgets_values\":[512,512,1]},{\"id\":7,\"type\":\"CLIPTextEncode\",\"pos\":[460,930],\"size\":{\"0\":425.27801513671875,\"1\":180.6060791015625},\"flags\":{},\"order\":5,\"mode\":0,\"inputs\":[{\"name\":\"clip\",\"type\":\"CLIP\",\"link\":16}],\"outputs\":[{\"name\":\"CONDITIONING\",\"type\":\"CONDITIONING\",\"links\":[6],\"slot_index\":0}],\"properties\":{\"Node name for S&R\":\"CLIPTextEncode\"},\"widgets_values\":[\"text, watermark\"]},{\"id\":10,\"type\":\"LoraLoader\",\"pos\":[434,162],\"size\":{\"0\":315,\"1\":126},\"flags\":{},\"order\":3,\"mode\":0,\"inputs\":[{\"name\":\"model\",\"type\":\"MODEL\",\"link\":11},{\"name\":\"clip\",\"type\":\"CLIP\",\"link\":14}],\"outputs\":[{\"name\":\"MODEL\",\"type\":\"MODEL\",\"links\":[12],\"shape\":3,\"slot_index\":0},{\"name\":\"CLIP\",\"type\":\"CLIP\",\"links\":[15,16],\"shape\":3,\"slot_index\":1}],\"properties\":{\"Node name for S&R\":\"LoraLoader\"},\"widgets_values\":[\"LCM_LoRA_Weights_SD15.safetensors\",1,1]},{\"id\":6,\"type\":\"CLIPTextEncode\",\"pos\":[430,580],\"size\":{\"0\":422.84503173828125,\"1\":164.31304931640625},\"flags\":{},\"order\":4,\"mode\":0,\"inputs\":[{\"name\":\"clip\",\"type\":\"CLIP\",\"link\":15}],\"outputs\":[{\"name\":\"CONDITIONING\",\"type\":\"CONDITIONING\",\"links\":[4],\"slot_index\":0}],\"properties\":{\"Node name for S&R\":\"CLIPTextEncode\"},\"widgets_values\":[\"masterpiece, best quality, 1girl, solo,long_hair,\\nlooking_at_viewer, smile, bangs, skirt, shirt, long_sleeves, hat,\\ndress, bow, holding, closed_mouth, flower, frills, hair_flower\\npetals, bouquet, holding_flower, center_frills, boonnet\\nholding bouquet, flower field, flower field, lineart,monochrome,\"]},{\"id\":8,\"type\":\"VAEDecode\",\"pos\":[1330,180],\"size\":{\"0\":210,\"1\":46},\"flags\":{},\"order\":7,\"mode\":0,\"inputs\":[{\"name\":\"samples\",\"type\":\"LATENT\",\"link\":7},{\"name\":\"vae\",\"type\":\"VAE\",\"link\":17}],\"outputs\":[{\"name\":\"IMAGE\",\"type\":\"IMAGE\",\"links\":[9],\"slot_index\":0}],\"properties\":{\"Node name for S&R\":\"VAEDecode\"}},{\"id\":4,\"type\":\"CheckpointLoaderSimple\",\"pos\":[54,453],\"size\":{\"0\":315,\"1\":98},\"flags\":{},\"order\":1,\"mode\":0,\"outputs\":[{\"name\":\"MODEL\",\"type\":\"MODEL\",\"links\":[11],\"slot_index\":0},{\"name\":\"CLIP\",\"type\":\"CLIP\",\"links\":[14],\"slot_index\":1},{\"name\":\"VAE\",\"type\":\"VAE\",\"links\":[],\"slot_index\":2}],\"properties\":{\"Node name for S&R\":\"CheckpointLoaderSimple\"},\"widgets_values\":[\"anythingelseV4_v45.safetensors\"]},{\"id\":11,\"type\":\"VAELoader\",\"pos\":[1011,550],\"size\":{\"0\":315,\"1\":58},\"flags\":{},\"order\":2,\"mode\":0,\"outputs\":[{\"name\":\"VAE\",\"type\":\"VAE\",\"links\":[17],\"shape\":3,\"slot_index\":0}],\"properties\":{\"Node name for S&R\":\"VAELoader\"},\"widgets_values\":[\"orangemixs_abyss2Sfw.safetensors\"]},{\"id\":9,\"type\":\"SaveImage\",\"pos\":[1571,184],\"size\":{\"0\":210,\"1\":58},\"flags\":{},\"order\":8,\"mode\":0,\"inputs\":[{\"name\":\"images\",\"type\":\"IMAGE\",\"link\":9}],\"properties\":{},\"widgets_values\":[\"ComfyUI\"]},{\"id\":3,\"type\":\"KSampler\",\"pos\":[937,171],\"size\":{\"0\":315,\"1\":262},\"flags\":{},\"order\":6,\"mode\":0,\"inputs\":[{\"name\":\"model\",\"type\":\"MODEL\",\"link\":12},{\"name\":\"positive\",\"type\":\"CONDITIONING\",\"link\":4},{\"name\":\"negative\",\"type\":\"CONDITIONING\",\"link\":6},{\"name\":\"latent_image\",\"type\":\"LATENT\",\"link\":2}],\"outputs\":[{\"name\":\"LATENT\",\"type\":\"LATENT\",\"links\":[7],\"slot_index\":0}],\"properties\":{\"Node name for S&R\":\"KSampler\"},\"widgets_values\":[156680208700286,\"randomize\",5,5,\"euler\",\"normal\",1]}],\"links\":[[2,5,0,3,3,\"LATENT\"],[4,6,0,3,1,\"CONDITIONING\"],[6,7,0,3,2,\"CONDITIONING\"],[7,3,0,8,0,\"LATENT\"],[9,8,0,9,0,\"IMAGE\"],[11,4,0,10,0,\"MODEL\"],[12,10,0,3,0,\"MODEL\"],[14,4,1,10,1,\"CLIP\"],[15,10,1,6,0,\"CLIP\"],[16,10,1,7,0,\"CLIP\"],[17,11,0,8,1,\"VAE\"]],\"groups\":[],\"config\":{},\"extra\":{},\"version\":0.4}}}}";
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
            // 发送 POST 请求并获取响应
            ResponseEntity<String> responseEntity = restTemplate.exchange(uri, httpMethod, requestEntity, String.class);
            // 获取响应体
            String responseBody = responseEntity.getBody();
            // 打印响应体
            System.out.println("Response: " + responseBody);
        } catch (Exception e) {
            log.error("出错了");
        }

    }
}
