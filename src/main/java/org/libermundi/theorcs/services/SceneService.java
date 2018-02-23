package org.libermundi.theorcs.services;

import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;
import org.libermundi.theorcs.domain.jpa.scene.Scene;

import java.util.List;

public interface SceneService extends BaseService<Scene> {
    List<Scene> getAllScenes(Chronicle chronicle);
}
