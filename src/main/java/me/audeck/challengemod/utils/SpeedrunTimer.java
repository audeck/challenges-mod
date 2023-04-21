package me.audeck.challengemod.utils;

import me.audeck.challengemod.cardinal.LevelComponents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.BackgroundHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.world.WorldProperties;

public class SpeedrunTimer extends DrawableHelper {
    private final MinecraftClient client;
    private final TextRenderer text_renderer;
    private ServerPlayerEntity player;
    private CompoundTag tag;
    private long ticks_initial;
    private long time_initial;
    private boolean paused = false;
    private long paused_at;
    private long unpaused_at;
    private long pause_time;
    private long time_add;
    private boolean ended = false;
    public static boolean FIRST_CYCLE;
    public static String TIME_PLAYED_STRING;

    public SpeedrunTimer(MinecraftClient client) {
        this.client = client;
        this.text_renderer = this.client.textRenderer;
    }

    private void firstCycle() {
        if (FIRST_CYCLE) {
            this.paused = false;
            this.paused_at = 0;
            this.unpaused_at = 0;
            this.time_add = 0;
            this.player = this.client.getServer().getPlayerManager().getPlayer(this.client.player.getName().getString());
            this.ticks_initial = this.player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.PLAY_ONE_MINUTE));
            this.tag = LevelComponents.CHALLENGE_PROPERTIES.get((WorldProperties) client.getServer().getSaveProperties()).getTag();
            this.time_initial = this.tag.getLong("time_played");
            this.pause_time = this.tag.getLong("pause_time");
            FIRST_CYCLE = false;
        }
    }

    private boolean getSeenCredits() {
        return this.tag.getBoolean("seen_credits");
    }

    private long ticksToMilliseconds(long ticks) {
        return ticks*50;
    }

    private long getTimePlayed() {
        long ticks_current = this.player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.PLAY_ONE_MINUTE));

        /* - Pause timing
        long time_current = System.currentTimeMillis();
        long temp_time_add = 0;

        if (this.client.isPaused()) {
            if (!this.paused) {
                this.paused_at = System.currentTimeMillis();
                this.pause_time = Math.min(this.pause_time + (time_current - this.unpaused_at)/3, 5000);
                this.paused = true;
            }
            temp_time_add = Math.max(time_current - (this.paused_at + this.pause_time), 0);
            this.tag.putLong("pause_time", Math.max(this.pause_time - (time_current - this.paused_at), 0));
        } else {
            if (this.paused) {
                temp_time_add = Math.max(time_current - (this.paused_at + this.pause_time), 0);
                this.unpaused_at = System.currentTimeMillis();
                this.time_add += temp_time_add;
                this.pause_time -= Math.min(time_current - this.paused_at, this.pause_time);
                this.paused = false;
            }
            this.tag.putLong("pause_time", Math.min(this.pause_time + (time_current - this.unpaused_at)/3, 5000));
        }

        long time = this.time_initial + ticksToMilliseconds(ticks_current - this.ticks_initial) + this.time_add + temp_time_add;
        this.tag.putLong("time_played", time);
        //this.tag.putLong("pause_time", this.pause_time);
        return time;
         */

        long time = this.time_initial + ticksToMilliseconds(ticks_current - this.ticks_initial);
        this.tag.putLong("time_played", time);
        return time;
    }

    private String formatTime(long time_in_milliseconds) {
        long milliseconds = time_in_milliseconds % 1000 - time_in_milliseconds % 50;
        long seconds = (time_in_milliseconds - (milliseconds)) / 1000 % 60;
        long minutes = (time_in_milliseconds - (milliseconds + seconds)) / 60000 % 60;
        long hours = (time_in_milliseconds - (milliseconds + seconds + minutes)) / 3600000 % 24;

        if (hours > 0) {
            return String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds) + "." + String.format("%03d", milliseconds);
        } else {
            return String.format("%02d", minutes) + ":" + String.format("%02d", seconds) + "." + String.format("%03d", milliseconds);
        }
    }

    private String formatTimePrecise(long time_in_milliseconds) {
        long milliseconds = time_in_milliseconds % 1000;
        long seconds = (time_in_milliseconds - (milliseconds)) / 1000 % 60;
        long minutes = (time_in_milliseconds - (milliseconds + seconds)) / 60000 % 60;
        long hours = (time_in_milliseconds - (milliseconds + seconds + minutes)) / 3600000 % 24;

        if (hours > 0) {
            return String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds) + "." + String.format("%03d", milliseconds);
        } else {
            return String.format("%02d", minutes) + ":" + String.format("%02d", seconds) + "." + String.format("%03d", milliseconds);
        }
    }

    public void render(MatrixStack matrix_stack) {
        firstCycle();
        this.client.getProfiler().push("speedrun_timer");

        if (!this.ended) {
            long time_played = this.getTimePlayed();
            TIME_PLAYED_STRING = this.formatTime(time_played);

            if (this.getSeenCredits()) {
                TIME_PLAYED_STRING = this.formatTimePrecise(time_played);
                this.ended = true;
            }
        }

        switch ((int)this.client.getWindow().getScaleFactor()) {
            case 1:
                this.draw1(matrix_stack);
                break;
            case 2:
                this.draw2(matrix_stack);
                break;
            case 3:
                this.draw3(matrix_stack);
                break;
            case 4:
                this.draw4(matrix_stack);
                break;
        }

        this.client.getProfiler().pop();
    }

    private void draw1(MatrixStack matrix_stack) {
        int w = this.text_renderer.getWidth(TIME_PLAYED_STRING);
        int x = this.client.getWindow().getScaledWidth() - 2 - w;
        int y = this.client.getWindow().getScaledHeight() - 10;
        int opacity = this.client.options.getTextBackgroundColor(0.8F);
        int color = 16777216 | (255 << 24 & -16777216);
        int x1 = this.client.getWindow().getScaledWidth() - 8 - 2*w;
        int y1 = this.client.getWindow().getScaledHeight() - 24;
        int x2 = this.client.getWindow().getScaledWidth() - 2;
        int y2 = this.client.getWindow().getScaledHeight() - 2;

        //Background
        matrix_stack.push();
        matrix_stack.translate(this.client.getWindow().getScaledWidth() - 1F * this.client.getWindow().getScaledWidth() + 1,
                               this.client.getWindow().getScaledHeight() - 1F * this.client.getWindow().getScaledHeight() + 1, 0);
        matrix_stack.scale(1F, 1F, 1F);
        fill(matrix_stack, x1, y1, x2, y2, BackgroundHelper.ColorMixer.mixColor(opacity, color));
        matrix_stack.pop();

        // Timer
        matrix_stack.push();
        matrix_stack.translate(this.client.getWindow().getScaledWidth() - 2F * this.client.getWindow().getScaledWidth() + 1,
                               this.client.getWindow().getScaledHeight() - 2F * this.client.getWindow().getScaledHeight() + 1, 0);
        matrix_stack.scale(2F, 2F, 2F);
        //this.text_renderer.draw(matrix_stack, this.ticks_played_string, x, y, 16777215);
        this.text_renderer.drawWithShadow(matrix_stack, TIME_PLAYED_STRING, x, y, 16777215);
        matrix_stack.pop();
    }

    private void draw2(MatrixStack matrix_stack) {
        int w = this.text_renderer.getWidth(TIME_PLAYED_STRING);
        int x = this.client.getWindow().getScaledWidth() - 2 - w;
        int y = this.client.getWindow().getScaledHeight() - 10;
        int opacity = this.client.options.getTextBackgroundColor(0.8F);
        int color = 16777216 | (255 << 24 & -16777216);
        int x1 = this.client.getWindow().getScaledWidth() - 13 - 3*w;
        int y1 = this.client.getWindow().getScaledHeight() - 57;
        int x2 = this.client.getWindow().getScaledWidth() - 4;
        int y2 = this.client.getWindow().getScaledHeight() - 4;

        //Background
        matrix_stack.push();
        matrix_stack.translate(this.client.getWindow().getScaledWidth() - 0.5F * this.client.getWindow().getScaledWidth() + 1,
                               this.client.getWindow().getScaledHeight() - 0.5F * this.client.getWindow().getScaledHeight() + 1, 0);
        matrix_stack.scale(0.5F, 0.5F, 0.5F);
        fill(matrix_stack, x1, y1, x2, y2, BackgroundHelper.ColorMixer.mixColor(opacity, color));
        matrix_stack.pop();

        // Header
        matrix_stack.push();
        matrix_stack.translate(this.client.getWindow().getScaledWidth() - 1F * this.client.getWindow().getScaledWidth() + 0,
                               this.client.getWindow().getScaledHeight() - 1F * this.client.getWindow().getScaledHeight() - 1F, 0);
        matrix_stack.scale(1F, 1F, 1F);
        //this.text_renderer.draw(matrix_stack, "Combined Time", x2 - (x2 - x1)/4 - 32, y-14, 16777215);
        this.text_renderer.drawWithShadow(matrix_stack, "In-Game Time", x2 - (x2 - x1)/4 - 29, y-14, 16777215);
        matrix_stack.pop();

        // Timer
        matrix_stack.push();
        matrix_stack.translate(this.client.getWindow().getScaledWidth() - 1.5F * this.client.getWindow().getScaledWidth() + 0.5F,
                               this.client.getWindow().getScaledHeight() - 1.5F * this.client.getWindow().getScaledHeight() + 0.5F, 0);
        matrix_stack.scale(1.5F, 1.5F, 1.5F);
        //this.text_renderer.draw(matrix_stack, this.ticks_played_string, x, y, 16777215);
        this.text_renderer.drawWithShadow(matrix_stack, TIME_PLAYED_STRING, x, y, 16777215);
        matrix_stack.pop();
    }

    private void draw3(MatrixStack matrix_stack) {
        int w = this.text_renderer.getWidth(TIME_PLAYED_STRING);
        int x = this.client.getWindow().getScaledWidth() - 2 - w;
        int y = this.client.getWindow().getScaledHeight() - 10;
        int opacity = this.client.options.getTextBackgroundColor(0.8F);
        int color = 16777216 | (255 << 24 & -16777216);
        int x1 = this.client.getWindow().getScaledWidth() - 18 - 4*w;
        int y1 = this.client.getWindow().getScaledHeight() - 72;
        int x2 = this.client.getWindow().getScaledWidth() - 6;
        int y2 = this.client.getWindow().getScaledHeight() - 6;

        //Background
        matrix_stack.push();
        matrix_stack.translate(this.client.getWindow().getScaledWidth() - 1/3F * this.client.getWindow().getScaledWidth() + 1,
                               this.client.getWindow().getScaledHeight() - 1/3F * this.client.getWindow().getScaledHeight() + 1, 0);
        matrix_stack.scale(1/3F, 1/3F, 1/3F);
        fill(matrix_stack, x1, y1, x2, y2, BackgroundHelper.ColorMixer.mixColor(opacity, color));
        matrix_stack.pop();

        // Header
        matrix_stack.push();
        matrix_stack.translate(this.client.getWindow().getScaledWidth() - 2/3F * this.client.getWindow().getScaledWidth() + 1,
                               this.client.getWindow().getScaledHeight() - 2/3F * this.client.getWindow().getScaledHeight() - 4/3F, 0);
        matrix_stack.scale(2/3F, 2/3F, 2/3F);
        //this.text_renderer.draw(matrix_stack, "Time", x2 - (int)(x2 - x1)/4.225F - 33, y-19, y-14, 16777215);
        this.text_renderer.drawWithShadow(matrix_stack, "In-Game Time", x2 - (int)((x2 - x1)/4.225F) - 32, y-19, 16777215);
        matrix_stack.pop();

        // Timer
        matrix_stack.push();
        matrix_stack.translate(this.client.getWindow().getScaledWidth() - 4/3F * this.client.getWindow().getScaledWidth() + 1/3F,
                               this.client.getWindow().getScaledHeight() - 4/3F * this.client.getWindow().getScaledHeight() + 1/3F, 0);
        matrix_stack.scale(4/3F, 4/3F, 4/3F);
        //this.text_renderer.draw(matrix_stack, this.ticks_played_string, x, y, 16777215);
        this.text_renderer.drawWithShadow(matrix_stack, TIME_PLAYED_STRING, x, y, 16777215);
        matrix_stack.pop();
    }

    private void draw4(MatrixStack matrix_stack) {
        int w = this.text_renderer.getWidth(TIME_PLAYED_STRING);
        int x = this.client.getWindow().getScaledWidth() - 2 - w;
        int y = this.client.getWindow().getScaledHeight() - 11;
        int opacity = this.client.options.getTextBackgroundColor(0.8F);
        int color = 16777216 | (255 << 24 & -16777216);
        int x1 = this.client.getWindow().getScaledWidth() - 23 - 5*w;
        int y1 = this.client.getWindow().getScaledHeight() - 96;
        int x2 = this.client.getWindow().getScaledWidth() - 8;
        int y2 = this.client.getWindow().getScaledHeight() - 11;

        if (this.client.options.fullscreen) {
            //Background
            matrix_stack.push();
            matrix_stack.translate(this.client.getWindow().getScaledWidth() - 1/4F * this.client.getWindow().getScaledWidth() + 1,
                    this.client.getWindow().getScaledHeight() - 1/4F * this.client.getWindow().getScaledHeight() + 2, 0);
            matrix_stack.scale(1/4F, 1/4F, 1/4F);
            fill(matrix_stack, x1, y1, x2, y2, BackgroundHelper.ColorMixer.mixColor(opacity, color));
            matrix_stack.pop();

            // Header
            matrix_stack.push();
            matrix_stack.translate(this.client.getWindow().getScaledWidth() - 3/4F * this.client.getWindow().getScaledWidth() + 1,
                    this.client.getWindow().getScaledHeight() - 3/4F * this.client.getWindow().getScaledHeight() - 1/3F, 0);
            matrix_stack.scale(3/4F, 3/4F, 3/4F);
            //this.text_renderer.draw(matrix_stack, "Time", x2 - (int)(x2 - x1)/4.225F - 33, y-19, y-14, 16777215);
            this.text_renderer.drawWithShadow(matrix_stack, "In-Game Time", (int) ((x2 - (x2 - x1) / 6.7F) - 30), y - 15, 16777215);
            matrix_stack.pop();

            // Timer
            matrix_stack.push();
            matrix_stack.translate(this.client.getWindow().getScaledWidth() - 5/4F * this.client.getWindow().getScaledWidth() + 1/4F,
                    this.client.getWindow().getScaledHeight() - 5/4F * this.client.getWindow().getScaledHeight() + 7/4F, 0);
            matrix_stack.scale(5/4F, 5/4F, 5/4F);
            //this.text_renderer.draw(matrix_stack, this.ticks_played_string, x, y, 16777215);
            this.text_renderer.drawWithShadow(matrix_stack, TIME_PLAYED_STRING, x, y, 16777215);
            matrix_stack.pop();
        } else {
            //Background
            matrix_stack.push();
            matrix_stack.translate(this.client.getWindow().getScaledWidth() - 1/4F * this.client.getWindow().getScaledWidth() + 1,
                    this.client.getWindow().getScaledHeight() - 1/4F * this.client.getWindow().getScaledHeight() + 1, 0);
            matrix_stack.scale(1/4F, 1/4F, 1/4F);
            fill(matrix_stack, x1, y1, x2, y2, BackgroundHelper.ColorMixer.mixColor(opacity, color));
            matrix_stack.pop();

            // Header
            matrix_stack.push();
            matrix_stack.translate(this.client.getWindow().getScaledWidth() - 3/4F * this.client.getWindow().getScaledWidth() + 1,
                    this.client.getWindow().getScaledHeight() - 3/4F * this.client.getWindow().getScaledHeight() - 4/3F, 0);
            matrix_stack.scale(3/4F, 3/4F, 3/4F);
            //this.text_renderer.draw(matrix_stack, "Time", x2 - (int)(x2 - x1)/4.225F - 33, y-19, y-14, 16777215);
            this.text_renderer.drawWithShadow(matrix_stack, "In-Game Time", (int) ((x2 - (x2 - x1) / 6.7F) - 30), y - 15, 16777215);
            matrix_stack.pop();

            // Timer
            matrix_stack.push();
            matrix_stack.translate(this.client.getWindow().getScaledWidth() - 5/4F * this.client.getWindow().getScaledWidth() + 1/4F,
                    this.client.getWindow().getScaledHeight() - 5/4F * this.client.getWindow().getScaledHeight() + 3/4F, 0);
            matrix_stack.scale(5/4F, 5/4F, 5/4F);
            //this.text_renderer.draw(matrix_stack, this.ticks_played_string, x, y, 16777215);
            this.text_renderer.drawWithShadow(matrix_stack, TIME_PLAYED_STRING, x, y, 16777215);
            matrix_stack.pop();
        }
    }
}
