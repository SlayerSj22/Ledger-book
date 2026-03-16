import { useEffect, useState, useCallback } from "react";
import api from "../services/api";
import AccountCard from "../components/AccountCard";

function AccountsPage() {

  const [accounts, setAccounts] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(false);

  const size = 10;

  const fetchAccounts = useCallback(async (pageNumber) => {

    try {

      setLoading(true);

      const res = await api.get(
        `/account/pending?page=${pageNumber}&size=${size}`
      );

      const pageData = res.data.data;
      
      
      setAccounts(pageData.content || []);
      setTotalPages(pageData.totalPages || 0);

    } catch (err) {

      console.error("Error fetching accounts", err);

    } finally {

      setLoading(false);

    }

  }, []);

  useEffect(() => {
    fetchAccounts(page);
  }, [page, fetchAccounts]);

  return (
    <div className="max-w-6xl mx-auto p-6">

      <h1 className="text-2xl font-bold mb-6">
        Accounts
      </h1>

      {/* Loading */}
      {loading && (
        <p className="text-gray-500 mb-4">
          Loading accounts...
        </p>
      )}

      {/* Account Cards */}
      <div className="flex flex-col gap-4">

        {accounts.map((account) => (
          <AccountCard
            key={account.id}
            account={account}
            refresh={() => fetchAccounts(page)}
          />
        ))}

        {accounts.length === 0 && !loading && (
          <p className="text-gray-500">
            No pending accounts found.
          </p>
        )}

      </div>

      {/* Pagination */}
      {totalPages > 1 && (
        <div className="flex justify-center items-center gap-4 mt-8">

          <button
            disabled={page === 0}
            onClick={() => setPage((prev) => prev - 1)}
            className="px-4 py-2 border rounded disabled:opacity-40"
          >
            Prev
          </button>

          <span>
            Page {page + 1} / {totalPages}
          </span>

          <button
            disabled={page + 1 >= totalPages}
            onClick={() => setPage((prev) => prev + 1)}
            className="px-4 py-2 border rounded disabled:opacity-40"
          >
            Next
          </button>

        </div>
      )}

    </div>
  );
}

export default AccountsPage;