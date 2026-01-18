package fr.rammex.simpleuhc.threads;

import fr.rammex.simpleuhc.SimpleUHC;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import java.util.ArrayList;
import java.util.List;

public abstract class ChunkLoaderThread implements Runnable {

    private final World world;
    private final int chunksPerTick;  // Nombre de chunks à charger par tick
    private final int restDuration;

    private final int maxChunk;
    private int x, z;
    private final int totalChunksToLoad;
    private int chunksLoaded;

    // File d'attente pour les chunks
    private final List<ChunkCoord> chunkQueue;
    private int queueIndex = 0;

    public ChunkLoaderThread(World world, int size, int chunksPerTick, int restDuration) {
        this.world = world;
        this.chunksPerTick = chunksPerTick;  // Charge plusieurs chunks par tick
        this.restDuration = restDuration;

        maxChunk = Math.round(size / 16f) + 1;
        totalChunksToLoad = (2 * maxChunk + 1) * (2 * maxChunk + 1);

        // Pré-calcule tous les chunks en spirale pour un chargement optimal
        chunkQueue = generateSpiralPattern();

        x = -maxChunk;
        z = -maxChunk;
    }

    public abstract void onDoneLoadingWorld();
    public abstract void onDoneLoadingChunk(Chunk chunk);

    // Génère un pattern en spirale pour un chargement plus naturel
    private List<ChunkCoord> generateSpiralPattern() {
        List<ChunkCoord> coords = new ArrayList<>();
        int x = 0, z = 0;
        int dx = 0, dz = -1;

        for (int i = 0; i < totalChunksToLoad; i++) {
            if ((-maxChunk <= x && x <= maxChunk) && (-maxChunk <= z && z <= maxChunk)) {
                coords.add(new ChunkCoord(x, z));
            }

            if (x == z || (x < 0 && x == -z) || (x > 0 && x == 1-z)) {
                int temp = dx;
                dx = -dz;
                dz = temp;
            }

            x += dx;
            z += dz;
        }

        return coords;
    }

    @Override
    public void run() {
        int loaded = 0;

        // Charge plusieurs chunks par tick au lieu d'un seul
        while (queueIndex < chunkQueue.size() && loaded < chunksPerTick) {
            ChunkCoord coord = chunkQueue.get(queueIndex);

            // Utilise loadChunk(x, z, true) qui force la génération
            Chunk chunk = world.getChunkAt(coord.x, coord.z);

            // Pour Spigot 1.8, load synchrone
            if (!chunk.isLoaded()) {
                chunk.load(true);
            }

            onDoneLoadingChunk(chunk);

            loaded++;
            queueIndex++;
            chunksLoaded++;
        }

        // Vérifier si le plugin est toujours activé
        if (!SimpleUHC.instance.isEnabled()) {
            Bukkit.getLogger().info("[SimpleUHC] Plugin désactivé, arrêt de la génération !");
            return;
        }

        // Continuer le chargement ou terminer
        if (queueIndex < chunkQueue.size()) {
            // Log tous les 5% au lieu de chaque batch
            if (chunksLoaded % (totalChunksToLoad / 20) == 0) {
                Bukkit.getLogger().info("[SimpleUHC] Chargement de la map " + getLoadingState() + "% - " +
                        chunksLoaded + "/" + totalChunksToLoad + " chunks chargés");
            }

            // Pause plus courte entre les batches
            Bukkit.getScheduler().scheduleSyncDelayedTask(SimpleUHC.instance, this, restDuration);
        } else {
            // Tous les chunks sont chargés
            Bukkit.getLogger().info("[SimpleUHC] Génération terminée ! 100% - " +
                    chunksLoaded + " chunks chargés");
            Bukkit.getScheduler().runTask(SimpleUHC.instance, this::onDoneLoadingWorld);
        }
    }

    public void printSettings() {
        Bukkit.getLogger().info("[SimpleUHC] Génération de l'environnement " + world.getEnvironment().toString());
        Bukkit.getLogger().info("[SimpleUHC] Chargement de " + totalChunksToLoad + " chunks au total");
        Bukkit.getLogger().info("[SimpleUHC] Pattern: Spirale optimisée");
        Bukkit.getLogger().info("[SimpleUHC] Vitesse: " + chunksPerTick + " chunks par tick");
        Bukkit.getLogger().info("[SimpleUHC] Pause de " + restDuration + " ticks entre chaque batch");
        Bukkit.getLogger().info("[SimpleUHC] Chargement de la map " + getLoadingState() + "%");
    }

    private String getLoadingState() {
        double percentage = 100 * (double) chunksLoaded / totalChunksToLoad;
        return world.getEnvironment() + " " + (Math.floor(10 * percentage) / 10);
    }

    // Classe helper pour les coordonnées
    private static class ChunkCoord {
        final int x, z;
        ChunkCoord(int x, int z) {
            this.x = x;
            this.z = z;
        }
    }
}