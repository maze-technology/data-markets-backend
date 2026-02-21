create table if not exists public.markets_dataproviders_metadatas
(
  id               bigserial primary key,
  market_id         uuid not null,
  data_provider_id  uuid not null,
  extra_datas       jsonb,
  tool_box          jsonb,
  created_at        timestamp with time zone not null default now(),
  constraint fk_markets_dataproviders_metadatas_market
    foreign key (market_id)
    references public.markets(id)
    on delete cascade
);

create unique index if not exists index_unique_markets_dataproviders_metadatas_market_provider
  on public.markets_dataproviders_metadatas(market_id, data_provider_id);
