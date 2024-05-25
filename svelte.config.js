import adapter from "@sveltejs/adapter-vercel";
import { vitePreprocess } from "@sveltejs/kit/vite";
import dotenv from "dotenv";

dotenv.config({ path: "./.env.local" });
dotenv.config({ path: "./.env" });

process.env.PUBLIC_VERSION = process.env.npm_package_version;

/** @type {import('@sveltejs/kit').Config} */
const config = {
	preprocess: vitePreprocess(),
	kit: {
		adapter: adapter({
			// Puedes configurar las opciones del adaptador node aquí
			out: "public", // Directorio de salida
			precompress: false, // Habilitar o deshabilitar la compresión previa
			envPrefix: "PUBLIC_", // Prefijo para variables de entorno expuestas públicamente
			maxDuration: 160,
		}),
		paths: {
			base: process.env.APP_BASE || "",
		},
		csrf: {
			checkOrigin: false,
		},
	},
};

export default config;
