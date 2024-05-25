import ChatThumbnail from "./ChatThumbnail.svelte";
import { collections } from "$lib/server/database";
import { error, type RequestHandler } from "@sveltejs/kit";
import { ObjectId } from "mongodb";
import type { SvelteComponent } from "svelte";

import { Resvg } from "@resvg/resvg-js";
import satori from "satori";
import { html } from "satori-html";
import sharp from "sharp";

export const GET: RequestHandler = (async ({ params }) => {
	const assistant = await collections.assistants.findOne({
		_id: new ObjectId(params.assistantId),
	});

	if (!assistant) {
		throw error(404, "Assistant not found.");
	}

	let avatar = "";
	const fileId = collections.bucket.find({ filename: assistant._id.toString() });
	const file = await fileId.next();
	if (file) {
		avatar = await (async () => {
			const fileStream = collections.bucket.openDownloadStream(file?._id);

			const fileBuffer = await new Promise<Buffer>((resolve, reject) => {
				const chunks: Uint8Array[] = [];
				fileStream.on("data", (chunk) => chunks.push(chunk));
				fileStream.on("error", reject);
				fileStream.on("end", () => resolve(Buffer.concat(chunks)));
			});

			return fileBuffer;
		})()
			.then(async (buf) => sharp(buf).jpeg().toBuffer()) // convert to jpeg bc satori png is really slow
			.then(async (buf) => "data:image/jpeg;base64," + buf.toString("base64"));
	}

	const renderedComponent = (ChatThumbnail as unknown as SvelteComponent).render({
		name: assistant.name,
		description: assistant.description,
		createdByName: assistant.createdByName,
		avatar,
	});

	const reactLike = html(
		"<style>" + renderedComponent.css.code + "</style>" + renderedComponent.html
	);

	const svg = await satori(reactLike, {
		width: 1200,
		height: 648,
		fonts: [], // Provide an empty array if not using custom fonts
	});

	const png = new Resvg(svg, {
		fitTo: { mode: "original" },
	})
		.render()
		.asPng();

	return new Response(png, {
		headers: {
			"Content-Type": "image/png",
		},
	});
}) satisfies RequestHandler;
