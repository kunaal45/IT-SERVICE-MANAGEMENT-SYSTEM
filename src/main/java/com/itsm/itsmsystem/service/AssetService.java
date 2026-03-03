package com.itsm.itsmsystem.service;

import com.itsm.itsmsystem.model.entity.Asset;
import com.itsm.itsmsystem.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetService {

    @Autowired
    private AssetRepository assetRepository;

    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    public Asset getAssetById(Long id) {
        return assetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asset not found"));
    }

    public Asset createAsset(Asset asset) {
        if (assetRepository.findByAssetCode(asset.getAssetCode()).isPresent()) {
            throw new RuntimeException("Asset code already exists");
        }
        return assetRepository.save(asset);
    }
}
