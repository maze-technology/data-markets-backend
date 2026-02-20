insert into public.markets_dataproviders_metadatas (
  market_id,
  data_provider_id,
  extra_datas,
  tool_box,
  created_at
)
select
  m.id,
  nullif(coalesce(md ->> 'dataProviderId', md ->> 'data_provider_id'), '')::uuid as data_provider_id,
  coalesce(md -> 'extraDatas', md -> 'extra_datas', '{}'::jsonb) as extra_datas,
  coalesce(md -> 'toolBox', md -> 'tool_box', '{}'::jsonb) as tool_box,
  m.created_at
from public.markets m
cross join lateral jsonb_array_elements(coalesce(m.data_providers_meta_datas, '[]'::jsonb)) as md
where nullif(coalesce(md ->> 'dataProviderId', md ->> 'data_provider_id'), '') is not null
on conflict do nothing;
