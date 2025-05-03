import { readdirSync, readFileSync } from 'node:fs';
import { resolve, dirname }         from 'node:path';
import { fileURLToPath }            from 'node:url';
import type { Plugin, ViteDevServer } from 'vite';

function mergeLang(dir: string) {
  const merged: Record<string, unknown> = {};
  for (const f of readdirSync(dir).filter(f => f.endsWith('.json'))) {
    Object.assign(merged, JSON.parse(readFileSync(resolve(dir, f), 'utf8')));
  }
  return JSON.stringify(merged);
}

export default function jhipsterI18nMerge(): Plugin {
  const __dirname = dirname(fileURLToPath(import.meta.url));

  const base = resolve(__dirname, '../../public/i18n');

  return {
    name: 'jhipster-i18n-merge',

    /* --- DEV ---------------------------------------------------------------- */
    configureServer(server: ViteDevServer) {
      server.middlewares.use('/i18n', (req, res, next) => {
        const m = req.url?.match(/^\/([a-z-]+)\.json/);
        if (!m) return next();

        try {
          const json = mergeLang(resolve(base, m[1]));
          res.setHeader('Content-Type', 'application/json');
          res.end(json);
        } catch (e) {
          next(e);
        }
      });
    },

    resolveId(id) {
      if (id.startsWith('/i18n/') && id.endsWith('.json')) return id;
    },
    load(id) {
      if (id.startsWith('/i18n/') && id.endsWith('.json')) {
        const lang = id.split('/').at(-1)!.replace('.json', '');
        return mergeLang(resolve(base, lang));
      }
    },

    generateBundle() {
      for (const lang of readdirSync(base)) {
        this.emitFile({
          type: 'asset',
          fileName: `i18n/${lang}.json`,
          source: mergeLang(resolve(base, lang)),
        });
      }
    },
  };
}
