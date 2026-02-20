create table if not exists public.markets
(
  id                        uuid primary key default gen_random_uuid(),
  type                      varchar(255) not null,
  exchange                  varchar(255) not null,
  base                      varchar(255) not null,
  quote                     varchar(255) not null,
  option_specific_data      jsonb,
  data_providers_meta_datas jsonb not null default '[]'::jsonb,
  created_at                timestamp with time zone not null default now()
);

alter table if exists public.markets
  add column if not exists option_specific_data jsonb;

alter table if exists public.markets
  add column if not exists data_providers_meta_datas jsonb not null default '[]'::jsonb;

create unique index if not exists index_unique_markets_type_exchange_base_quote
  on public.markets (type, upper(exchange), upper(base), upper(quote));

create index if not exists index_markets_data_providers_meta_datas_gin
  on public.markets using gin (data_providers_meta_datas);
