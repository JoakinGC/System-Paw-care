import { sveltekit } from "@sveltejs/kit/vite";
import { defineConfig, type PluginOption } from "vite";
import Icons from "unplugin-icons/vite";
import { promises } from "fs";

// Plugin para cargar archivos TTF como ArrayBuffer
function loadTTFAsArrayBuffer(): PluginOption {
	return {
		name: "load-ttf-as-array-buffer",
		async transform(_src, id) {
			if (id.endsWith(".ttf")) {
				const buffer = await promises.readFile(id);
				return `export default new Uint8Array([${Array.from(
					new Uint8Array(buffer)
				).toString()}]).buffer`;
			}
		},
	};
}

export default defineConfig({
	plugins: [
		sveltekit(),
		Icons({
			compiler: "svelte",
		}),
		loadTTFAsArrayBuffer(),
	],
	optimizeDeps: {
		include: ["browser-image-resizer"],
	},
	build: {
		outDir: "public", // Especifica el directorio de salida como 'public'
		rollupOptions: {
			external: ["sharp"],
		},
	},
});
