package org.libermundi.theorcs.services.scene;

import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;
import org.libermundi.theorcs.domain.jpa.scene.Scene;
import org.libermundi.theorcs.services.base.BaseService;

import java.util.List;

public interface SceneService extends BaseService<Scene> {
    List<Scene> getAllScenes(Chronicle chronicle);
}
