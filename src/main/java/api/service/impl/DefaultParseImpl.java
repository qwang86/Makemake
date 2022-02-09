package api.service.impl;

import api.service.InterfaceParseService;
import api.utils.YamlUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultParseImpl implements InterfaceParseService {

    public static final  Logger logger = LoggerFactory.getLogger(DefaultParseImpl.class);

    @Override
    public String getBasePath(String intefaceName) {
        String prefix = "basepath";
        return YamlUtil.getConfig(intefaceName, prefix);
    }

    @Override
    public String getMethod(String intefaceName) {
        String prefix = "method";
        return YamlUtil.getConfig(intefaceName, prefix);
    }

    @Override
    public String getMINE(String intefaceName) {
        String prefix = "contentType";
        return YamlUtil.getConfig(intefaceName, prefix);
    }
}
