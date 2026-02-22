create table if not exists public.markets
(
  id                   uuid primary key default gen_random_uuid(),
  type                 varchar(255) not null,
  exchange             varchar(255) not null,
  base                 varchar(255) not null,
  quote                varchar(255) not null,
  option_specific_data jsonb,
  created_at           timestamp with time zone not null default now()
);

create unique index if not exists index_unique_markets_type_exchange_base_quote
  on public.markets (type, upper(exchange), upper(base), upper(quote));
